package br.com.api;

import java.util.ArrayList;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.api.UserRoutes;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

public class UserRoutes {
    
    public static void processarRotas(ServicoUsuario servicoUsuario){
        Spark.post("/cadastrar", adicionarUsuario(servicoUsuario));
        Spark.get("/consultar/:id", obterUsuarioPorId(servicoUsuario));
        Spark.get("/consultar", obterTodosUsuarios(servicoUsuario));
        Spark.put("/alterar/:id", alterarUsuario(servicoUsuario));
        Spark.delete("/excluir/:id", excluirUsuario(servicoUsuario));
    }
    
    // Método para lidar com a rota de adicionar usuário
    private static Route adicionarUsuario(ServicoUsuario servicoUsuario) {
        return new Route() {
            @Override
            public Object handle(Request request, Response response) throws Exception {

                ObjectMapper converteJson = new ObjectMapper();

                String nome = request.queryParams("nome");
                String email = request.queryParams("email");

                Usuario usuario = servicoUsuario.adicionar(nome, email);

                response.status(201); // 201 Created
                return converteJson.writeValueAsString(usuario);
            }
        };
    }

    // Método para lidar com a rota de buscar usuário por ID
    private static Route obterUsuarioPorId(ServicoUsuario servicoUsuario) {
        return new Route() {
            @Override
            public Object handle(Request request, Response response) throws Exception {
                ObjectMapper converteJson = new ObjectMapper();

                Usuario usuario = servicoUsuario.obterPorId(request.params(":id"));

                if (usuario != null) {
                    response.status(200); // 200 OK
                    return converteJson.writeValueAsString(usuario);
                } else {
                    response.status(204); // 204 No Content
                    return converteJson.writeValueAsString(new ArrayList<>());
                }
            }
        };
    }

    // Método para lidar com a rota de buscar todos os usuários
    private static Route obterTodosUsuarios(ServicoUsuario servicoUsuario) {
        return new Route() {
            @Override
            public Object handle(Request request, Response response) throws Exception {
                ObjectMapper converteJson = new ObjectMapper();

                ArrayList<Usuario> usuarios = (ArrayList<Usuario>) servicoUsuario.obterTodos();

                if (usuarios.isEmpty()) 
                    return converteJson.writeValueAsString(new ArrayList<>());
                else 
                    return converteJson.writeValueAsString(usuarios);
            }
        };
    }

    // Método para lidar com a rota de atualizar usuário
    private static Route alterarUsuario(ServicoUsuario servicoUsuario) {
        return new Route() {
            @Override
            public Object handle(Request request, Response response) throws Exception {
                ObjectMapper converteJson = new ObjectMapper();
                
                String id = request.params(":id");
                Usuario usuario = servicoUsuario.obterPorId(id);
                if (usuario != null) {
                    String nome = request.queryParams("nome");
                    String email = request.queryParams("email");

                    servicoUsuario.atualizar(id, nome, email);

                    response.status(200); // 200 Ok

                    return """
                            { 
                                "message": "Usuário com id ${id} foi atualizado com sucesso."
                            } 
                            """;
                } else {
                    response.status(204); // 404 Not Found
                    return converteJson.writeValueAsString(new ArrayList<>());
                }
            }
        };
    }

    // Método para lidar com a rota de excluir usuário
    private static Route excluirUsuario(ServicoUsuario servicoUsuario) {
        return new Route() {
            @Override
            public Object handle(Request request, Response response) throws Exception {
                ObjectMapper converteJson = new ObjectMapper();
                
                String id = request.params(":id");
                Usuario usuario = servicoUsuario.obterPorId(id);

                if (usuario != null) {
                    servicoUsuario.excluir(id);
                                        
                    return """
                            { 
                                "message": "Usuário com id ${id} foi excluído com sucesso."
                            } 
                            """;

                } else {
                    response.status(404); // 404 Not Found
                    return converteJson.writeValueAsString(new ArrayList<>());
                }
            }
        };
    }
}
