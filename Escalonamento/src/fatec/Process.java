package fatec;

import java.util.ArrayList;

public class Process implements Comparable<Process> {

	public int id, burst, tCheg, tEsp,tC;
	public ArrayList<Integer> tParada, tChegada,fila;

	


	public void srtf() {

		int ant = fila.get(0);
		tChegada.add(fila.get(0));
		int i = 1;
		for ( i = 1; i < fila.size(); i++) {
			int cont = fila.get(i);
			if (ant+1 == cont) {
				ant = cont;
				//continue;
			}
			else {
				tParada.add(fila.get(i-1)+1);
				tChegada.add(fila.get(i));
				ant = fila.get(i);
			}
			if (i == fila.size()-1)
				tParada.add(fila.get(i)+1);
			
		}
		calculaTempo();
		
		
	}
	
	public void calculaTempo() {
		int x = 0, y = 0;
		for (Integer integer : tChegada) {
			x += integer;
		}
		for (int c = 0; c < (tParada.size() - 1); c++) {
			y += tParada.get(c);
		}

		int soma = x - y;
		System.out.println("P" + id + "\nTempo Medio de espera: " + soma);
		try {
			System.out.println("Turnaround: " + tParada.get(tParada.size() - 1));
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public Process(int id, int burst) {
		this.id = id;
		this.burst = burst;
		tEsp = 0;
		tParada = new ArrayList<>();
	}

	public Process(int id, int burst, int tCheg) {
		this.id = id;
		this.burst = burst;
		this.tCheg = tCheg;
		tEsp = 0;
		tParada = new ArrayList<>();
		tChegada = new ArrayList<>();
		fila = new ArrayList<>();
	}

	public int gettEsp() {
		return tEsp;
	}

	public void settEsp(int tEsp) {
		this.tEsp = tEsp;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getBurst() {
		return burst;
	}

	public void setBurst(int burst) {
		this.burst = burst;
	}

	public int gettCheg() {
		return tCheg;
	}

	public void settCheg(int tCheg) {
		this.tCheg = tCheg;
	}

	@Override
	public int compareTo(Process p) {
		if ((this.tCheg <= p.gettCheg()) && (this.getBurst() <= p.getBurst())) {
			return -1;
		} else if ((this.tCheg >= p.gettCheg() && this.getBurst() >= p.getBurst())) {
			return 1;
		} else {
			return 0;
		}
	}

	@Override
	public String toString() {
		return "P:" + id + "   burst: " + burst + "   TChegada: " + tCheg + "\n";
	}
}
