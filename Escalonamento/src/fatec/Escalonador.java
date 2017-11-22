package fatec;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;

public class Escalonador {
	public Scanner scan = new Scanner(System.in);
	String entrada;
	public ArrayList<Process> lista = new ArrayList<>();
	public ArrayList<Process> listaOrd = new ArrayList<>();
	public int n = 0, j, i, tEsp[] = new int[20], quantum; /* Tempo de espera */
	public float mEsp = 0, /* Media de espera */
			mResp = 0;/* Media de resposta */

	// Tela com quantidade de Processos
	public void entrada(int i, int k) {
		// Tela de entrada para digitar a quantidade de processos
		System.out.println("Numero de processos (maximo 20): ");
		n = scan.nextInt();
		if (k == 1) {
			System.out.println("Quantum: ");
			quantum = scan.nextInt();
		}

		// variaveis locais para armazenar os valores antes de instaciar a
		// classe processo
		int burst, tcheg;
		System.out.println("Definir tamanho do burst de cada processo?(S/N)");
		String op = scan.next();
		if (op.toUpperCase().equals("S")) {
			// Para cada processo ele vai pedir o tamanho do burst e o tempo de
			// chegada
			for (int l = 0; l < n; l++) {
				System.out.println("P" + (l + 1) + " :");
				burst = scan.nextInt();
				// se i for 1 entao nao pede o tempo de chegada
				if (i == 1) {
					lista.add(new Process(l + 1, burst));
				} else if (i == 2) {
					System.out.println("Tempo de Chegada P" + (l + 1) + " :");
					tcheg = scan.nextInt();
					lista.add(new Process(l + 1, burst, tcheg));
				}
			}
			// escolhe aleatorio os valores
		} else {
			Random rd = new Random();
			for (int l = 0; l < n; l++) {
				burst = 1 + rd.nextInt(25); // Aleatï¿½rio de 1 a 25
				if (i == 1)
					lista.add(new Process(l + 1, burst));
				else if (i == 2) {
					tcheg = rd.nextInt(25);
					lista.add(new Process(l + 1, burst, tcheg));

				}
			}
		}
	}

	// Calcula e imprimi os resultados
	public void imprimir(ArrayList<Process> p) {
		tEsp[0] = 0;
		int cont = 0;
		// Calcular o tempo de espera
		for (i = 1; i < n; i++) {
			tEsp[i] = 0;
			for (j = 0; j < i; j++)
				tEsp[i] += p.get(j).getBurst();
			cont += p.get(i).getBurst();
		}
		// Calcular a media de espera
		for (i = 0; i < n; i++) {
			int tCheg = p.get(i).getBurst() + tEsp[i];
			p.get(i).settCheg(tCheg);
			mEsp += tEsp[i];
			mResp += p.get(i).gettCheg();
			System.out.println("P" + p.get(i).getId() + " \t| Burst: " + p.get(i).getBurst() + "  \t  | T.Espera: "
					+ tEsp[i] + " " + "   | T.Chegada: " + p.get(i).gettCheg());
		}
		mEsp /= i;
		mResp /= i;
		System.out.println("\nTempo medio de espera: " + mEsp);
		System.out.println("Tempo medio de entrega: " + mResp);
		System.out.println();
	}

	// Metodo para fazer a ordenaïcao por burst e tempo de chegada
	public ArrayList<Process> ordenar(ArrayList<Process> li) {
		// Ordena a lista para garantir a hierarquia de chegada
		Collections.sort(li); // metodo sobrescrito na Classe Process
		return li;
	}

	// Simulador do escalonamento FCFS
	public void FCFS() {
		System.out.println("First Come, First Served");
		entrada(1, 0);
		imprimir(lista);
	}

	// Simulador do escalonamento SJF
	public void SJF() {
		System.out.println("Shortest Job First");
		entrada(1, 0);
		imprimir(ordenar(lista));
	}

	/*
	 * Entrada (x,y) x == 1 le só burst | x == 2 le burst e tempo de chegada | y
	 * == 1 le quantum | y == 0 nao le quantum
	 */
	
	public void SRTF() {
		System.out.println("Shortest Remaining Time First");
		int indice, t = 0;
		entrada(2, 0);
		ArrayList<Process> listaaux = new ArrayList<Process>(ordenar(lista));

		
		for (Process process : listaaux) {
			System.out.println("P" + process.id + "   | burst: " + process.burst 
					+ "      |Tempo de Chegada: " + process.tCheg);
			t += process.burst;
			process.tC = process.tCheg;
		}
		
		indice = 0;
		ArrayList<Process> filaId = new ArrayList<>();
		//
		int tC = 0;
		for (int i = 0; i < t;tC++ ) {
			if (listaaux.get(indice).tC > tC) {
				i++;
				for (Process process : listaaux) {
					if (process.tCheg > 0) process.tCheg -= 1;
				}
			}
			else if (listaaux.get(indice).burst > 0) {
				listaaux.get(indice).burst -= 1;
				listaaux.get(indice).fila.add(i);
				filaId.add(new Process(listaaux.get(indice).id,1));
				for (Process process : listaaux) {
					if (process.tCheg > 0) process.tCheg -= 1;
				}
				i++;
			}
			else if (listaaux.get(indice).burst == 0) {
				indice = qualIndice(++indice);
			}
			listaaux = ordenar(listaaux);
		}
		filaId.add(new Process(-1,0));
		System.out.println("Consumindo: ");
		for (int i = 1, k = 0; i < filaId.size(); i++) {
			if (filaId.get(k).id == filaId.get(i).id) {
				filaId.get(k).burst += 1;
				continue;
			}
			//else {
				System.out.println("P"+filaId.get(k).id+": " + filaId.get(k).burst);
				k = i;
				//}
		}
		

		System.out.println();
		for (Process process : listaaux) {
			process.srtf();
		}

	}

	public int qualIndice(int idx) {
		if (idx >= n)
			return 0;
		else
			return idx;
	}

	public void resolveRR(ArrayList<Process> listaaux) {
		int indice = 0, t = 0;
		//listaaux = ordenar(listaaux);
		for (Process process : listaaux) {
			t+= process.burst;
			System.out.println("P" + process.id + "   | burst: " + process.burst + "       |Tempo de Chegada: " + process.tCheg);
		}
		System.out.println("Consumindo: ");
		for (int i = 0; i < t;) {
			int c = indice;
			// Encontrar o proximo processo a chegar
			while (i < listaaux.get(indice).tCheg & listaaux.get(indice).burst != 0) {
				indice = qualIndice(++indice);
				if (c == indice)
					i++;
			}
			
			// Caso o burst seja maior que o quantum, consome o burst
			if (listaaux.get(indice).burst >= quantum) {
				System.out.println("P" + (listaaux.get(indice).id) + ": " + quantum);
				listaaux.get(indice).burst -= quantum; // consumindo o burst
				listaaux.get(indice).tChegada.add(i);
				i += quantum;
				listaaux.get(indice).tParada.add(i);
				indice = qualIndice(++indice);

			} // Caso seja menor, consome o burst inteiro e avança o i a quantia
				// de burts que havia
			else if (listaaux.get(indice).burst > 0) {
				System.out.println("P" + (listaaux.get(indice).id) + ": " + listaaux.get(indice).burst);
				listaaux.get(indice).tChegada.add(i);
				i += listaaux.get(indice).burst;
				listaaux.get(indice).tParada.add(i);
				listaaux.get(indice).burst = 0;
				indice = qualIndice(++indice);
			} // burst igual a zero, vai pro proximo processo
			else
				indice = qualIndice(++indice);
		}

		System.out.println();
		for (Process process : listaaux) {
			process.calculaTempo();
		}
	}

	// Round Robin
	public void RR() {
		System.out.println("Round Robin");
		entrada(2, 1);
		resolveRR(lista);
	}

	// RR com SJF
	public void MultiNivel() {
		System.out.println("Multinivel - Round Robin com Shortest Job First");
		entrada(2, 1);
		boolean [] entrou = new boolean[n];
		for (boolean b : entrou) {
			b = false;
		}
		ArrayList<Process> listaaux = new ArrayList<Process>(lista);
		int indice = 0, t = 0;
		//listaaux = ordenar(listaaux);
		for (Process process : listaaux) {
			t+= process.burst;
			System.out.println("P" + process.id + "   | burst: " + process.burst + "       |Tempo de Chegada: " + process.tCheg);
		}
		int tC = 0;
		
		System.out.println("Consumindo: \nCamada 1 - RR");
		for (int i = 0; i < t;) {
			int c = indice;
			// Encontrar o proximo processo a chegar
			while (i < listaaux.get(indice).tCheg & listaaux.get(indice).burst != 0) {
				indice = qualIndice(++indice);
				if (c == indice)
					i++;
			}
			
			// Caso o burst seja maior que o quantum, consome o burst
			if (listaaux.get(indice).burst >= quantum) {
				System.out.println("P" + (listaaux.get(indice).id) + ": " + quantum);
				listaaux.get(indice).burst -= quantum; // consumindo o burst
				listaaux.get(indice).tChegada.add(i);
				entrou[indice] = true;
				i += quantum;
				listaaux.get(indice).tParada.add(i);
				indice = qualIndice(++indice);

			} // Caso seja menor, consome o burst inteiro e avança o i a quantia
				// de burts que havia
			else if (listaaux.get(indice).burst > 0) {
				System.out.println("P" + (listaaux.get(indice).id) + ": " + listaaux.get(indice).burst);
				listaaux.get(indice).tChegada.add(i);
				entrou[indice] = true;
				i += listaaux.get(indice).burst;
				listaaux.get(indice).tParada.add(i);
				listaaux.get(indice).burst = 0;
				indice = qualIndice(++indice);
			} // burst igual a zero, vai pro proximo processo
			else
				indice = qualIndice(++indice);
			
			boolean k = false;
			for (boolean b : entrou) {
				if (b == false) {
					k = false;
					break;
				}
				k = true;
			}
			if (k == true) {
				tC = i;
				break;
			}
		}
		//========================================
		System.out.println("Camada 2 - SJF");
		listaaux = ordenar(listaaux);
		for (int i = 0; i < n; i++) {
			if ( listaaux.get(i).burst > 0) {
			System.out.println("P" + listaaux.get(i).id + ": " + listaaux.get(i).burst);
			listaaux.get(i).tChegada.add(tC);
			tC += listaaux.get(i).burst;
			listaaux.get(i).tParada.add(tC);
			}
			
		}

		System.out.println();
		for (Process process : listaaux) {
			process.calculaTempo();
		}

	}

}
