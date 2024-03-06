package com.example.calculaconta.repositories;

import com.example.calculaconta.models.Pedido;

import java.util.ArrayList;
import java.util.List;

public class PedidoRepository {
    private static PedidoRepository instance;
    private int id = 1;
    private List<Pedido> listaPedidos;

    private PedidoRepository() {
        listaPedidos = new ArrayList<>();
    }

    public static synchronized PedidoRepository getInstance() {
        if (instance == null) {
            instance = new PedidoRepository();
        }
        return instance;
    }

    public List<Pedido> getListaDePedidos() {
        return listaPedidos;
    }

    public Pedido adicionarPedido(Pedido pedido) {
        pedido.setId(id++);
        listaPedidos.add(pedido);
        return pedido;
    }

    public void clearList(){
        listaPedidos.clear();
        id = 1;
    }

    public Pedido getPedidoById(int idPedido){
        Pedido pedido = listaPedidos.stream().filter(p->p.getId()==idPedido).findFirst().orElse(null);
        return pedido;
    }
}
