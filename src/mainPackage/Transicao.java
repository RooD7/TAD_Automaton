/*
 * 
 * Trabalho I - LFA		-	23/05/2017
 * Rafaela Martins 		- 	
 * Rodrigo Sousa 		-	0011264
 * 
 */
package mainPackage;

public class Transicao {

	private Integer from;
	private Integer to;
	private char value;

	public Transicao() {
		this.from = -1;
		this.to = -1;
		this.value = '.';
	}
	
	//From
	public Integer getFrom() {
		return this.from;
	}
	public void setFrom(int from) {
		this.from = from;
	}
	
	//To
	public Integer getTo() {
		return this.to;
	}
	public void setTo(int to) {
		this.to = to;
	}
	
	//Value
	public char getValue() {
		return this.value;
	}
	public void setValue(char value) {
		this.value = value;
	}

}
