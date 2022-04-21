package Model.GestPedidos;

import java.io.Serializable;

public class Equipamento implements Serializable {
    private int codEquip;
    private String nifCliente;
    private String status;  // por_reparar / em_reparacao / reparado / irreparavel / abandonado / entregue / rejeitado


    public Equipamento(int codEquip, String nifCliente, String status){
        this.codEquip = codEquip;
        this.nifCliente = nifCliente;
        this.status = status;
    }

    public Equipamento(Equipamento e){
        //this.codEquip = e.getCodEquip();
        this.nifCliente = e.getNifCliente();
        this.status = e.getStatus();
    }

    public int getCodEquip() {
        return codEquip;
    }

    public String getNifCliente() {
        return nifCliente;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String toString() {
        return "Equipamento{ Código Equipamento: " + this.codEquip +
                " | NIF Cliente: " + this.nifCliente +
                " | Status: " + this.status;
    }

    public boolean equals(Object o){
        if (o == this) return true;
        if ((o == null) || (o.getClass() != this.getClass())) return false;

        Equipamento a = (Equipamento) o;

        return (a.getCodEquip() == this.codEquip);
    }

    public Equipamento clone(){
        return new Equipamento(this);
    }


}
