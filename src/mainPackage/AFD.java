/*
 * 
 * Trabalho I - LFA		-	23/05/2017
 * Rafaela Martins 		- 	
 * Rodrigo Sousa 		-	0011264
 * 
 */
package mainPackage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;



public class AFD {
	
	private ArrayList<Integer> estado;	//Estados
	private ArrayList<Character> alfa;	//Afabeto
	private ArrayList<Transicao> trans;	//Transicoes
	private int estadoInicial;	//Estado Inicial
	private ArrayList<Integer> estadoFinal;	//Estados Finais
	
	public AFD() {
		this.estado = new ArrayList<>();
		this.alfa = new ArrayList<>();
		this.trans = new ArrayList<>();
		this.estadoInicial = -1;
		this.estadoFinal = new ArrayList<>();
	}

	//Carrega arquivo de entrada	---	OK!
	public void Load(String nomeArq) throws JDOMException, IOException {
		
		File f = new File(nomeArq);
        if (!f.exists()){
        	System.out.println("Arquivo "+nomeArq+" não existe!");
        	System.exit(0);
        }
        
		SAXBuilder builder = new SAXBuilder();
		Document doc = builder.build(f);
		             
		// Pega Structure
		Element root = (Element) doc.getRootElement();
		
		//Automato nao eh AFD
		if(!root.getChildTextNormalize("type").equals("fa")) {
			System.out.println("Automato nao eh do tipo FA");
			System.exit(0);
		}
		
		// Pega Lista de States
		List<Element> states = root.getChild("automaton").getChildren("state");
		//Pega Lista de Transition
		List<Element> trst   = root.getChild("automaton").getChildren("transition");
		             
		Iterator<Element> i = states.iterator();
		while( i.hasNext() ){
			
	        Element state = (Element) i.next();
	        // Adiciona estado na lista de estados
	        this.getEstado().add(Integer.parseInt(state.getAttributeValue("id")));
	        
	        // Adiciona estado como estado inicial
	        if(state.getChild("initial") != null) {
	        	this.setEstadoInicial(Integer.parseInt(state.getAttributeValue("id")));
	        }
	        // Adiciona estado na lista de estados finais
	        if(state.getChild("final") != null) {
	        	this.getEstadoFinal().add(Integer.parseInt(state.getAttributeValue("id")));	        
	        }
		}
		
		String s;
		Iterator<?> j = trst.iterator();
		while( j.hasNext() ){
	        
			Transicao t = new Transicao();
	        Element trs = (Element) j.next();
	        // set FROM, TO e VALUE de t
	        t.setFrom(Integer.parseInt(trs.getChildText("from")));
	        t.setTo(Integer.parseInt(trs.getChildText("to")));
	        //String to Char
	        s = trs.getChildText("read");
	        t.setValue(s.charAt(0));

	        // Adiciona simbolo na lista do alfabeto
	        if(!this.getAlfa().contains(Character.valueOf(s.charAt(0)))) {
	        	this.getAlfa().add(Character.valueOf(s.charAt(0)));
	        }
	        
	        // Adiciona t no array de transicoes
	        this.getTrans().add(t);
		}
	}
	
	// Carrega arquivo de saida	---	OK!
	public void Salve(String nomeArq) throws IOException {
		Document doc = new Document();
        
		Element root = new Element("structure");
		Element auto = new Element("automaton");
		Element type = new Element("type");
		type.setText("fa");
		root.addContent(type);
		root.addContent(auto);
		         
		double xl = 175.0;
		double yl = 150.0;
		for(Integer e : this.getEstado()) {
			Element state = new Element("state");
			Attribute id = new Attribute("id",e.toString());
			Attribute name = new Attribute("name","q"+e.toString());
			state.setAttribute(id);
			state.setAttribute(name);

			Element x = new Element("x");
			Element y = new Element("y");
			x.setText(Double.toString(xl));
			y.setText(Double.toString(yl));
			state.addContent(x);
			state.addContent(y);
			
			if(this.getEstadoInicial() == e) {
				Element init = new Element("initial");
				state.addContent(init);
	        }
	        if(this.getEstadoFinal().contains(e)) {
	        	Element fini = new Element("final");
				state.addContent(fini);
	        }

			auto.addContent(state);
			xl += 75;
			yl += 50;
		}

		//for(Transicao t : trans) {
		Iterator<Transicao> k = this.getTrans().iterator();
		while( k.hasNext() ) {
			Transicao t = (Transicao) k.next();
			Element trs = new Element("transition");
			Element from = new Element("from");
			Element to = new Element("to");
			Element read = new Element("read");

			from.setText(t.getFrom().toString());
			to.setText(t.getTo().toString());
			read.setText(Character.toString(t.getValue()));
			trs.addContent(from);
			trs.addContent(to);
			trs.addContent(read);

			auto.addContent(trs);
		}
         
		doc.setRootElement(root);
		         
		XMLOutputter xout = new XMLOutputter();
		OutputStream out = new FileOutputStream( new File(nomeArq));
		xout.output(doc , out);
	}

	// Adicionar estado	---	OK!
	public void addState(int id, boolean init, boolean fim) {
		this.getEstado().add(id);
		if(init)
			this.setEstadoInicial(id);
		if(fim)
			this.getEstadoFinal().add(id);
	}
	
	// Adicionar transicao	---	OK!
	public void addTransition(int from, int to, char c) {
		Transicao t = new Transicao();
		t.setFrom(from);
		t.setTo(to);
		t.setValue(c);
		this.getTrans().add(t);
	}
	
	// Deletar estado	---	OK!
	public void deleteState(Integer n) {
		
		if(this.getEstado().contains(n)) {
			// Remove estado n
			this.getEstado().remove(n);
			
			// Remove transicoes do estado n
			Transicao tAux = new Transicao();
			Iterator<Transicao> t = this.getTrans().iterator();
			while(t.hasNext()) {
				tAux = (Transicao) t.next();
				if((tAux.getFrom().equals(n)) || (tAux.getTo().equals(n))) {
					System.out.println("From: "+tAux.getFrom()+"\tTo: "+tAux.getTo()+"\tDeletar = "+this.getTrans().indexOf(tAux));
					try {
						t.remove();
					} catch (Exception e){
						System.out.println("Erro:: "+ e.toString());
					}
				}
			}
		}
		else
			System.out.println("Estado Invalido! Estado "+n);
	}
	
	// Deletar Transicao	---	OK!
	public void deleteTransition(int from, int to, char c) {
		
		Transicao tAux = new Transicao();
		Iterator<Transicao> t = getTrans().iterator();
		while(t.hasNext()) {
			tAux = (Transicao) t.next();
			if((tAux.getFrom().equals(from)) && (tAux.getTo().equals(to) && (tAux.getValue() == c))) {
				t.remove();
			}
		}
	}
	
	// equivalencia de automatos -- FALTA
//	public static boolean equivalents(AFD m1, AFD m2) {
//		
//		ArrayList<Transicao> t1 = new ArrayList<>();
//		ArrayList<Transicao> t2 = new ArrayList<>();
//		
//		m1 = m1.minimum();
//		m2 = m2.minimum();
//		// Problemas com nomes??
//		
//		return true;
//	}
	
	// equivalencia de estados --- OK!
	public ArrayList<Integer> equivalents() {
		ArrayList<Integer> eqv = new ArrayList<>();
		
		for (Integer e1 : this.getEstado()) {
			for (Integer e2 : this.getEstado()) {
				if((e1 != e2) &&  (e1 < e2)){
					if(((this.getEstadoFinal().equals(e1)) && (this.getEstadoFinal().equals(e1))) || ((!this.getEstadoFinal().equals(e1)) && (!this.getEstadoFinal().equals(e1)))) {
						if (this.statesEquivalents(this, e1, e2)) {
							System.out.println("e1: "+e1+", e2: "+e2);
							eqv.add(e1);
							eqv.add(e2);
						}
					}
				}
			}
		}
		return eqv;
	}
	
	// Minimizacao --- OK!
	public AFD minimum() {
		AFD mm = new AFD();
		ArrayList<Integer> eqv = new ArrayList<>();
		
		mm = removeEstadosVazioNulos(this);
		eqv = mm.equivalents();

		if(!eqv.isEmpty()) {			
			int index = 1;
			int tam = eqv.size();
			while(index <= tam ) {
				mm.setTrans(tranfereTransE2ToE1(mm.getTrans(), eqv.get(index-1), eqv.get(index))); 
				
				mm.deleteState(eqv.get(index));
				index += 2;
			}
		}
		
		return mm;
	}
	
	public ArrayList<Transicao> tranfereTransE2ToE1(ArrayList<Transicao> t, Integer e1, Integer e2) {
				
		for (Transicao tr : t) {
			if(tr.getFrom().equals(e2)) {
				tr.setFrom(e1);
			}
			
			if(tr.getTo().equals(e2)) {
				tr.setTo(e1);
			}
		}
		return t;
	}
	
	public boolean statesEquivalents(AFD m, Integer e1, Integer e2) {
		
		Integer to1, to2;
			
		// Ou e1 e e2 nao sao iniciais		
		if((m.getEstadoInicial() != e1.intValue()) && (m.getEstadoInicial() != e2.intValue())) {
			// percorre todo alfabeto do automato
			for (Character alfa : m.getAlfa()) {
				// to1 recebe TO, quando FROM = e1, e VALUE = alfa
				to1 = getToTrans(m, e1, alfa);
				to2 = getToTrans(m, e2, alfa);
				
				// Se to1 e to2 diferentes, estados nao equivalentes
				if(!to1.equals(to2)) {
					return false;
				}
			}
		}
		else
			return false;
		return true;
	}
		
	// Complemento de um automato	---	OK!
	public AFD complement() {
		AFD aux = new AFD();
		
		aux.setAlfa(this.getAlfa());
		aux.setEstado(this.getEstado());
		aux.setEstadoInicial(this.getEstadoInicial());
		aux.setTrans(this.getTrans());
		
		// Estados nao finais, se tornam finais
		for (Integer i : getEstado()) {
			if(!this.getEstadoFinal().contains(i.intValue())) {
				aux.getEstadoFinal().add(i.intValue());
			}
		}
		return aux;
	}
	
	// Uniao de Automatos	--- OK!
	public AFD union(AFD m) {		
		AFD aux = new AFD();
		// produto dos 2 automatos
		aux = m.produtoAFD(this, m);
		
		ArrayList<Integer> f1 = new ArrayList<>();
		ArrayList<Integer> f2 = new ArrayList<>();
		
		f1 = this.getEstadoFinal();
		f2 = m.getEstadoFinal();
		
		int k = 0;
		for (Integer e1 = 0; e1 < this.getEstado().size(); e1++) {
			for (Integer e2 = 0; e2 < this.getEstado().size(); e2++) {
				// posicoes [e1, e2] pertencem aos estados finais de f1 e f2
				if((f1.contains(e1)) && (f2.contains(e2))) {
					// adiciona o estado final ao automato aux na posicao k
					aux.getEstadoFinal().add((Integer)k);
				}
				k++;
			}
		}
		return aux;
	}
	
	// Intersecao de Automatos	--- OK!
	public AFD intersection(AFD m) {		
		AFD aux = new AFD();
		// produto dos 2 automatos
		aux = m.produtoAFD(this, m);
		
		ArrayList<Integer> f1 = new ArrayList<>();
		ArrayList<Integer> f2 = new ArrayList<>();
		
		f1 = this.getEstadoFinal();
		f2 = m.getEstadoFinal();
		
		int k = 0;
		for (Integer e1 = 0; e1 < this.getEstado().size(); e1++) {
			for (Integer e2 = 0; e2 < this.getEstado().size(); e2++) {
				// posicoes [e1, e2] pertencem aos estados finais de f1 e f2
				if((f1.contains(e1)) || (f2.contains(e2))) {
					// adiciona o estado final ao automato aux na posicao k
					aux.getEstadoFinal().add((Integer)k);
				}
				k++;
			}
		}
		return aux;
	}
	
	// Diferenca de automatos	--- OK!
	public AFD difference(AFD m) {
		// intersecao de m1 com o complemento de m2
		return this.intersection(m.complement());
	}
	
	// Produto de Automatos	---	OK!
	public AFD produtoAFD(AFD m1, AFD m2) {
		
		AFD auxM = new AFD();
		int tamM1 = m1.getEstado().size();
		int tamM2 = m2.getEstado().size();
		int iniM1 = m1.initial();
		int iniM2 = m2.initial();
		int[][] mapa = new int[tamM1][tamM2];
		
		// gera os States do automato
		int k = 0;
		for (int i = 0; i < tamM1; i++) {
			for (int j = 0; j < tamM2; j++) {
				//estado inicial
				if((i == iniM1) && (j == iniM2))
					auxM.addState(k, true, false);
				else
					auxM.addState(k, false, false);
				
				mapa[i][j] = k;
				k++;
			}
		}		
		
		Integer toM2,toM1;
		char simM1;

		// gera Transicoes do automato
		ArrayList<Transicao> t1 = new ArrayList<>();
		for (Integer e1 : m1.getEstado()) {
			for (Integer e2 : m2.getEstado()) {
				// Lista de transicao com o FROM igual a state e1
				t1.clear();
				t1 = listaFromState(m1, t1, e1);
				
				// Percorre todas as transicoes
				for (Transicao t : t1) {
					toM1 = t.getTo();
					simM1 = t.getValue();
					toM2 = getToTrans(m2, e2, simM1);
					
					// existe TO de M2, add transicao
					if(toM2 != null) {
						auxM.addTransition(mapa[e1][e2], mapa[toM1][toM2], simM1); 
					}
				}
				
			}
		}
		auxM = removeEstadosVazioNulos(auxM);
		return auxM;
	}
	
	// Retorna Lista de FROM's = state
	public ArrayList<Transicao> listaFromState(AFD m, ArrayList<Transicao> t, int state) {
		
		Transicao tAux = new Transicao();
		Iterator<Transicao> t1 = m.getTrans().iterator();
		while(t1.hasNext()) {
			tAux = (Transicao) t1.next();
			if(tAux.getFrom().equals((Integer)state)) {
				t.add(tAux);
			}
		}
		return t;
	}
	
	// Retorna Lista de FROM's = state
	public ArrayList<Transicao> listaFromState(AFD m, int state) {
		
		ArrayList<Transicao> t = new ArrayList<Transicao>();
		Transicao tAux = new Transicao();
		Iterator<Transicao> t1 = m.getTrans().iterator();
		while(t1.hasNext()) {
			tAux = (Transicao) t1.next();
			if(tAux.getFrom().equals((Integer)state)) {
				t.add(tAux);
			}
		}
		return t;
	}
	
	// Remove estados vazios e/ou inacessiveis 
	public AFD removeEstadosVazioNulos(AFD m) {
		
		boolean flagNulo = true;
		Transicao tAux = new Transicao();
		Iterator<Transicao> t1;
		
		// Percorre todas os estados
		for (Integer e1 = 0; e1 < m.getEstado().size(); e1++) {
			// Nao eh o estado Inicial
			if(!e1.equals((Integer)m.getEstadoInicial())) {
				flagNulo = true;
				// Procura as tansicoes TO e1
				t1 = m.getTrans().iterator();
				while(t1.hasNext()) {
					tAux = (Transicao) t1.next();
					// Estado nao eh nulo ou inacessivel
					if(tAux.getTo().equals(e1)) {
						flagNulo = false;
						break;
					}
				}
				// Remover estados nulos e inacessiveis
				if(flagNulo) {
					System.out.println("Estado deletado: "+e1);
					m.deleteState(e1);
				}
			
			}
		}
		return m;
	}
	
	// Retorna TO da transicao que possui FROM = from e VALUE = value
	public Integer getToTrans(AFD m1, Integer from, char value) {
		
		Transicao tAux = new Transicao();
		Iterator<Transicao> t = m1.getTrans().iterator();
		while(t.hasNext()) {
			tAux = (Transicao) t.next();
			if(tAux.getFrom().equals((Integer)from) && tAux.getValue() == value) {
				return tAux.getTo();
			}
		}
		return null;
	}

	// Retorna TO da transicao FROM = atual
	public int getStateTo( AFD m1, int atual, Iterator<Transicao> t) {
		
		Transicao tAux = new Transicao();
		tAux = (Transicao) t.next();
		if(tAux.getFrom().equals((Integer)atual)) {
			return tAux.getTo();
		}
		return -1;
	}
	
	// Retorna VALUE que possui FROM = from e TO = to
	public char getSimbolo( AFD m1, int from, int to) {
		
		Transicao tAux = new Transicao();
		Iterator<Transicao> t = m1.getTrans().iterator();
		while(t.hasNext()) {
			tAux = (Transicao) t.next();
			if(tAux.getFrom().equals((Integer)from) && tAux.getTo().equals((Integer)to)) {
				return tAux.getValue();
			}
		}
		return ' ';
	}
	
	// Percorre 1 estado do Automato
	public int percorreUmEstado(int atual, char c) {
		
		// Percorrer todas as transicoes
		Transicao tAux = new Transicao();
		Iterator<Transicao> t = getTrans().iterator();
		while(t.hasNext()) {
			tAux = (Transicao) t.next();
			if((tAux.getFrom().equals(atual)) && (tAux.getValue() == c)) {
				return tAux.getTo();
			}
		}
		return -1;
	}
	
	// Palavra percorre o Automato	---	OK!
	public boolean accept(String palavra) {
		
		char c;
		int estadoAtual = this.getEstadoInicial();
		
		for (int i=0; i< palavra.length(); i++) { 
			c = palavra.charAt(i);
			estadoAtual = percorreUmEstado(estadoAtual, c);
			if(estadoAtual == -1)
				return false;
		}
		if(this.getEstadoFinal().contains((Integer)estadoAtual))
			return true;

		return false;
	}
	
	// Retorna o Estado Inicial	---	OK!
	public int initial() {
		return getEstadoInicial();
	}
	
	// Retorna os Estados Finais	---	OK!
	public ArrayList<Integer> finals() {
		return getEstadoFinal();
	}
	
	// Testa a pertenca de uma palavra na linguagem	---	OK!
	public int move(int estado, String palavra) {
		
		char c;
		int estadoAtual = estado;
		
		for (int i=0; i< palavra.length(); i++) { 
			c = palavra.charAt(i);
			estadoAtual = percorreUmEstado(estadoAtual, c);
			if(estadoAtual == -1)
				return -1;
		}
		return estadoAtual;
	}
		
	// ---------------- GET's e SET's ------------------ //
	//Estados
	public ArrayList<Integer> getEstado() {
		return this.estado;
	}
	public void setEstado(ArrayList<Integer> estado) {
		this.estado = estado;
	}

	//Alfabeto
	public ArrayList<Character> getAlfa() {
		return alfa;
	}
	public void setAlfa(ArrayList<Character> alfa) {
		this.alfa = alfa;
	}

	//Transicoes
	public ArrayList<Transicao> getTrans() {
		return trans;
	}
	public void setTrans(ArrayList<Transicao> trans) {
		this.trans = trans;
	}

	//Estado Inicial
	public int getEstadoInicial() {
		return estadoInicial;
	}
	public void setEstadoInicial(int estadoInicial) {
		this.estadoInicial = estadoInicial;
	}

	//Estado Final
	public ArrayList<Integer> getEstadoFinal() {
		return estadoFinal;
	}
	public void setEstadoFinal(ArrayList<Integer> estadoFinal) {
		this.estadoFinal = estadoFinal;
	}
	
}