package br.com.api;

import spark.Spark; 
import spark.Request;
import spark.Response;
import spark.Route;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

public class Main {
    
    private static final UserService userService = new UserService();
    
    public static void main(String[] args) {
        Spark.port(8082);

        // Configuração do Thymeleaf
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setCharacterEncoding("UTF-8");
        
        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);

        // Habilitar CORS
        Spark.options("/*", new Route() {
            @Override
            public Object handle(Request requisicaoHttp, Response respostaHttp) throws Exception {
                String accessControlRequestHeaders = requisicaoHttp.headers("Access-Control-Request-Headers");
                if (accessControlRequestHeaders != null) {
                    respostaHttp.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
                }
                String accessControlRequestMethod = requisicaoHttp.headers("Access-Control-Request-Method");
                if (accessControlRequestMethod != null) {
                    respostaHttp.header("Access-Control-Allow-Methods", accessControlRequestMethod);
                }
                return "OK";
            }
        });

        Spark.before(new spark.Filter() {
            @Override
            public void handle(Request requisicaoHttp, Response respostaHttp) throws Exception {
                respostaHttp.header("Access-Control-Allow-Origin", "*"); // Permite todas as origens
                respostaHttp.header("Access-Control-Allow-Methods", "OPTIONS, GET, POST, PUT, DELETE");
                respostaHttp.header("Access-Control-Allow-Headers", "Content-Type, Authorization");
            }
        });

        UserRoutes.handleRoutes(userService, templateEngine);

    }
}
