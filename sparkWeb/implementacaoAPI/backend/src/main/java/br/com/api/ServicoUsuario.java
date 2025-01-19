package br.com.api;

import java.util.ArrayList;

public class ServicoUsuario {
    private ArrayList<Usuario> usuarios = new ArrayList<>();
    private int contadorId = 1;

    public Usuario adicionar(String nome, String email) {
        Usuario usuario = new Usuario(String.valueOf(contadorId++), nome, email);
        usuarios.add(usuario);
        return usuario;
    }

    public Usuario obterPorId(String id) {
        for (Usuario usuario : usuarios) {
            if (usuario.getId().equals(id)) {
                return usuario;
            }
        }
        return null;
    }

    public ArrayList<Usuario> obterTodos() {
        return usuarios;
    }

    public void atualizar(String id, String nome, String email) {
        Usuario usuario = obterPorId(id);
        if (usuario != null) {
            usuario.setNome(nome);
            usuario.setEmail(email);
        }
    }

    public void excluir(String id) {
        for (int i = 0; i < usuarios.size(); i++) {
            if (usuarios.get(i).getId().equals(id)) {
                usuarios.remove(i);
                break;
            }
        }
    }
}
