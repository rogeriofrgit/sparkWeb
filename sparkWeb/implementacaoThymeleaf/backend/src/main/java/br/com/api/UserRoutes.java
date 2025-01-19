package br.com.api;

import java.util.ArrayList;

import org.thymeleaf.TemplateEngine;

import br.com.api.UserRoutes;

import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

import org.thymeleaf.context.Context;

public class UserRoutes {
    
    public static void handleRoutes(UserService userService, TemplateEngine templateEngine){
        Spark.get("/", obterBarra(userService, templateEngine));
        Spark.post("/cadastrar", adicionarUsuario(userService, templateEngine));
        Spark.post("/alterar", alterarUsuario(userService, templateEngine));
        Spark.get("/excluir/:id", excluirUsuario(userService, templateEngine));
    }

    private static String construirPagina(UserService userService, TemplateEngine templateEngine){
        Context context = new Context();
                
        ArrayList<User> users = (ArrayList<User>) userService.findAll();

        context.setVariable("users", users);
        return templateEngine.process("index", context);
    }
    
    // Método para lidar com a rota de adicionar usuário
    private static Route obterBarra(UserService userService, TemplateEngine templateEngine) {
        return new Route() {
            @Override
            public Object handle(Request request, Response response) throws Exception {

                return construirPagina(userService, templateEngine);

            }
        };
    }
    
    // Método para lidar com a rota de adicionar usuário
    private static Route adicionarUsuario(UserService userService, TemplateEngine templateEngine) {
        return new Route() {
            @Override
            public Object handle(Request request, Response response) throws Exception {


                String name = request.queryParams("name");
                String email = request.queryParams("email");

                userService.add(name, email);

                return construirPagina(userService, templateEngine);
            }
        };
    }

    // Método para lidar com a rota de atualizar usuário
    private static Route alterarUsuario(UserService userService, TemplateEngine templateEngine) {
        return new Route() {
            @Override
            public Object handle(Request request, Response response) throws Exception {
                
                String id = request.queryParams("id");
                User user = userService.findById(id);

                if (user != null) {
                    String name = request.queryParams("name");
                    String email = request.queryParams("email");
                    userService.update(id, name, email);
                } 

                response.redirect("/");
                return null;
            }
        };
    }

    // Método para lidar com a rota de excluir usuário
    private static Route excluirUsuario(UserService userService, TemplateEngine templateEngine) {
        return new Route() {
            @Override
            public Object handle(Request request, Response response) throws Exception {
                
                String id = request.params(":id");
                User user = userService.findById(id);

                if (user != null) {
                    userService.delete(id);
                } 

                return construirPagina(userService, templateEngine);

            }
        };
    }
}
