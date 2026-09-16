class TanqueCombustivel{
private String tipoCombustivel;
private double volumeAtual;
private boolean vazamentoDetectado;
static double CONSUMO_PADRAO=15.0;

TanqueCombustivel(String tipo){
    tipoCombustivel = tipo;
    volumeAtual = 1000;
    vazamentoDetectado = false;
}

public String getTipoCombustivel(){
    return tipoCombustivel;
}

public double getVolumeAtual(){
    return volumeAtual;
}

public void setVolumeAtual(double volume){
    volumeAtual=volume;
}

public boolean isVazamentoDetectado(){
    return true;
}

public void setVazamentoDetectado(boolean status){
    vazamentoDetectado = status;
}

public String toString(){
    String statusVazamento=vazamentoDetectado ? "sim" : "nao";
    return "Tipo : [" + tipoCombustivel + "] - volume: [" + volumeAtual + "] - Status: [" + statusVazamento + "]";
}
public boolean equals (TanqueCombustivel obj){
    if(tipoCombustivel.equals(obj.getTipoCombustivel())){
        return true;
    }
    else{
        return false;
    }
}

public void injetarCombustivel(double qtd){
    if(vazamentoDetectado){
        volumeAtual += qtd/2;
    }else{
        volumeAtual += qtd;
    }
}

public void consumir(double qtd){
    if(qtd>volumeAtual){
        volumeAtual=0;
        vazamentoDetectado=true;
    }
    else{
        volumeAtual-=qtd;
    }
}

public void consumir(){
    consumir(CONSUMO_PADRAO);
}

public boolean verificarVazamento(){
    return vazamentoDetectado;
}
}