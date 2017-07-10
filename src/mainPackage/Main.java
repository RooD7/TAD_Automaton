/*
 * 
 * Trabalho I - LFA		-	23/05/2017
 * Rafaela Martins 		- 	
 * Rodrigo Sousa 		-	0011264
 * 
 */
package mainPackage;

import java.io.IOException;
//import java.util.ArrayList;

import org.jdom2.JDOMException;

public class Main {

	public static void main(String[] args) throws JDOMException, IOException {
		AFD m1 = new AFD();
		AFD m1C = new AFD();
		AFD m2 = new AFD();
		AFD m3 = new AFD();
		AFD m4 = new AFD();
		AFD m5 = new AFD();
		AFD m6 = new AFD();
		AFD mm = new AFD();
		//ArrayList<Integer> eqv = new ArrayList<Integer>();

		// load
		m1.Load("entrada.jff");
		m2.Load("entrada2.jff");
		// complement
		m1C = m1.complement();
		
		// equivalents
//		if (AFD.equivalents(m1, m2)) 
//			System.out.println("Equivalentes!");
//		else
//			System.out.println("Nao equivalentes!");
		
		// Apagar
		for (Integer e : m1.getEstado()) {
			System.out.println("Estados: "+e.intValue());
		}
		// addState, addTransition, deleteState, deleteTransition
		m2.addState(5, false, false);
		m2.addTransition(2, 2, 'b');
		m2.deleteState(2);
		m2.deleteState(3);
		m2.deleteTransition(0, 1, 'a');
		
		// Accept
		if(m1.accept("abaab"))
			System.out.println("Aceita!");
		else
			System.out.println("Rejeita!");
		
		if(m1.accept("b"))
			System.out.println("Aceita!");
		else
			System.out.println("Rejeita!");
		
		// initial, move, finals
		int estado = m1.initial();
		estado = m1.move(estado, "ab");
		if(m1.finals().contains((Integer)estado))
			System.out.println("Aceita!");
		else
			System.out.println("Rejeita!");
		
		// Apagar
		for (Integer e : m2.getEstado()) {
			System.out.println("Estados: "+e.intValue());
		}
		
		// equivalents
//		if (AFD.equivalents(m1, m2)) 
//			System.out.println("Equivalentes!");
//		else
//			System.out.println("Nao equivalentes!");
		
		m3 = m1.produtoAFD(m1, m2);
		
		// Uniao
		m4 = m1.union(m2);
		// intersecao
		m5 = m1.intersection(m2);
		// diferenca
		m6 = m1.difference(m2);
		//minimizacao
		//mm = m1.minimum();
		
		
		mm.Load("entradaM.jff");
		
//		eqv = mm.equivalents();
//		for (Integer e : eqv) {
//			System.out.print(e+", ");
//		}
//		mm = mm.minimum();
		
		// salve
		m1.Salve("saidaM1.jff");
		m1C.Salve("complementoM1.jff");
		m2.Salve("saidaM2.jff");
		m3.Salve("produtoM1eM2.jff");
		m4.Salve("uniaoM1eM2.jff");
		m5.Salve("intersecaoM1eM2.jff");
		m6.Salve("diferencaM1eM2.jff");
		mm.Salve("Minimizacao.jff");
		
	}

}
