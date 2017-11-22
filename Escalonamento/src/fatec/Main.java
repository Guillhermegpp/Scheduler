package fatec;


import java.util.Scanner;

import javax.swing.JOptionPane;

public class Main {
	public static void main(String[] args) {
		System.out.println(    "  ================ Sistemas Operacionais =============\n"
							 + " ============== Feito por Guilherme Pinto ==============");
		Scanner scan = new Scanner(System.in);
		boolean ciclo = true;
		while (ciclo) {
			Escalonador process = new Escalonador();
			System.out.println("=========Simulador de Escalonamento de Processos==========\n1 - FSFC\n2 - SJF\n3 - SRTF\n"
							+ "4 - Round RobinR\n5 - Multinível\n");
			int op = scan.nextInt();
			switch (op) {
			case 1:
				process.FCFS();/*FCFS*/
				break;
			case 2:
				process.SJF();/*SJF*/
				break;
			case 3:
				process.SRTF(); /*SRTF*/
				break;
			case 4: 
				process.RR();/*RR*/
				break;
			case 5:
				process.MultiNivel();/*RR*/
				break;
			case 6:
				ciclo = false;
				break;
			default:
				break;
			}

		}
	}
}
