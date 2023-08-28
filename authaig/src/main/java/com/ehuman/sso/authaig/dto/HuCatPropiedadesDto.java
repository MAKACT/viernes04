package com.ehuman.sso.authaig.dto;

public class HuCatPropiedadesDto {
	
private String producto;
	
	private String parametro;
	
	private Long numero_sec;
	
	private String valor;

	public String getProducto() {
		return producto;
	}

	public void setProducto(String producto) {
		this.producto = producto;
	}

	public String getParametro() {
		return parametro;
	}

	public void setParametro(String parametro) {
		this.parametro = parametro;
	}

	public Long getNumero_sec() {
		return numero_sec;
	}

	public void setNumero_sec(Long numero_sec) {
		this.numero_sec = numero_sec;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("HuCatPropiedadesDto [producto=");
		builder.append(producto);
		builder.append(", parametro=");
		builder.append(parametro);
		builder.append(", numero_sec=");
		builder.append(numero_sec);
		builder.append(", valor=");
		builder.append(valor);
		builder.append("]");
		return builder.toString();
	}
	
	
	

}
