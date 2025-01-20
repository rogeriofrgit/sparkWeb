package br.com.api;

import java.util.ArrayList;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.api.RotasUsuario;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

public class RotasUsuario {
    
    public static void processarRotas(ServicoUsuario servicoUsuario){
        //configura o spark com quais metodos devem ser executados 
        //quando cada rota for requisitada
        Spark.post("/cadastrar", adicionarUsuario(servicoUsuario));
        Spark.get("/consultar/:id", obterUsuarioPorId(servicoUsuario));
        Spark.get("/consultar", obterTodosUsuarios(servicoUsuario));
        Spark.put("/alterar/:id", alterarUsuario(servicoUsuario));
        Spark.delete("/excluir/:id", excluirUsuario(servicoUsuario));
        //Spark.get("/consultar/:email", obterUsuarioPorEmail(servicoUsuario));
    }
    

    //Metodo Esqueleto
    //XXXX: Nome do metodo que o usuario quer criar
    //YYYY: Parametros de entrada para o metodo
    //ZZZZ: Implementação do método
    //QQQQ: Status code do HTTP
    //SSSS: Informação que será retornado
    //
    // private static Route XXXX(YYYY) {
    //     return new Route() {
    //         @Override
    //         public Object handle(Request request, Response response) throws Exception {
    //
    //             ZZZZ
    //
    //             response.status(QQQQ); 
    //             return SSSS;
    //         }
    //     };
    // }

    // Método para lidar com a rota de adicionar usuário
    private static Route adicionarUsuario(ServicoUsuario servicoUsuario) {
        return new Route() {
            @Override
            public Object handle(Request request, Response response) throws Exception {
                //classe para converter objeto para json
                ObjectMapper converteJson = new ObjectMapper();

                //extrai os parametros no http boddy 
                String nome = request.queryParams("nome");
                String email = request.queryParams("email");

                //executa o metodo de adicionar o contato no array list
                Usuario usuario = servicoUsuario.adicionar(nome, email);

                //defini o status code do httpd
                response.status(201); // 201 Created

                //converte o objeto usuario para json e retorna via http response
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

                //extrai o parametro id da URL
                String id = request.params(":id");

                //busca o contato no array list pela id
                Usuario usuario = servicoUsuario.obterPorId(id);

                if (usuario != null) {
                    //defini o http status code
                    response.status(200); // 200 OK

                    //retorna o objeto encontrado no formato json
                    return converteJson.writeValueAsString(usuario);
                } else {
                    //defini o http status code
                    response.status(204); // 204 No Content

                    //retorna um array list vazio no formato json
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

                //busca todos os contatos cadastrados no array list
                //é necessário realizar um cast explicito para converter o retorno 
                //do servicoUsuario
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

                    return "{\"message\": \"Usuário com id " + id + " foi atualizado com sucesso.\"}";
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
                                        
                    return "{\"message\": \"Usuário com id " + id + " foi excluido com sucesso.\"}";
                } else {
                    response.status(404); // 404 Not Found
                    return converteJson.writeValueAsString(new ArrayList<>());
                }
            }
        };
    }
}
