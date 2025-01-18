package src;

import static java.lang.Math.exp;
import static java.lang.Math.min;

public class BodyBattery {

    /**
     * Atualiza a bateria corporal de forma gradual, dividindo a recarga em múltiplas etapas.
     * Apenas as duas últimas etapas podem atingir o limite máximo.
     *
     * @param bateriaInicial A bateria inicial do indivíduo (em %).
     * @param recargaTotal   A quantidade total de recarga a ser aplicada (em pontos).
     * @param limiteMaximo   O limite máximo da bateria (em %).
     * @param etapas         O número total de etapas para dividir a recarga.
     * @param k              A constante que controla a rapidez da recarga.
     */
    static void atualizarBateriaGradual(
            double bateriaInicial,
            double recargaTotal,
            double limiteMaximo,
            int etapas,
            double k
    ) {
        double bateriaAtual = bateriaInicial;
        double recargaPorEtapa = recargaTotal / etapas;
        int etapasLimite = 2; // Número de etapas permitidas para atingir o limite

        System.out.print("\n=== Simulação ===\n");
        System.out.printf("Bateria Inicial: %.2f%%\n", bateriaInicial);
        System.out.printf("Recarga Total: %.2f pontos divididos em %d etapas de %.2f pontos cada\n", recargaTotal, etapas, recargaPorEtapa);
        System.out.println("---------------------------------------------------------------------------");
        System.out.printf("%-7s %-20s %-15s\n", "Etapa", "Recarga Escalada", "Bateria Final");
        System.out.println("---------------------------------------------------------------------------");

        for (int i = 1; i <= etapas; i++) {
            // Determina se está nas últimas duas etapas
            boolean nasUltimasEtapas = (i > (etapas - etapasLimite));

            if (nasUltimasEtapas) {
                if (i == etapas - 1) {
                    // Etapa penúltima: aplicar metade da recarga restante
                    double recargaRestante = limiteMaximo - bateriaAtual;
                    double recargaEscalada = recargaRestante / etapasLimite;

                    // Garante que a recarga não ultrapasse o restante
                    recargaEscalada = min(recargaEscalada, recargaRestante);

                    // Atualiza a bateria
                    bateriaAtual += recargaEscalada;

                    // Assegura que a bateria não ultrapasse o limite
                    if (bateriaAtual > limiteMaximo) {
                        recargaEscalada = limiteMaximo - (bateriaAtual - recargaEscalada);
                        bateriaAtual = limiteMaximo;
                    }

                    System.out.printf("%-7d %-20.2f %-15.2f\n", i, recargaEscalada, bateriaAtual);
                }
                else if (i == etapas) {
                    // Última etapa: aplicar a recarga restante para atingir o limite
                    double recargaEscalada = limiteMaximo - bateriaAtual;

                    // Garante que a recarga não ultrapasse o restante
                    recargaEscalada = min(recargaEscalada, limiteMaximo - bateriaAtual);

                    // Atualiza a bateria
                    bateriaAtual += recargaEscalada;

                    // Assegura que a bateria não ultrapasse o limite
                    if (bateriaAtual > limiteMaximo) {
                        recargaEscalada = limiteMaximo - (bateriaAtual - recargaEscalada);
                        bateriaAtual = limiteMaximo;
                    }

                    System.out.printf("%-7d %-20.2f %-15.2f\n", i, recargaEscalada, bateriaAtual);
                }
            } else {
                // Etapas 1 a E-2: aplicar a função exponencial para escalar a recarga
                double capacidadeRestante = limiteMaximo - bateriaAtual;

                // Calcula a recarga escalada usando a função exponencial
                double recargaEscalada = capacidadeRestante * (1 - exp(-k * recargaPorEtapa));

                // Calcula a recarga que precisa ser reservada para as últimas etapas
                double recargaToReserve = (limiteMaximo - bateriaAtual) / etapasLimite;

                // Garante que a recarga escalada não consuma a recarga reservada
                recargaEscalada = min(recargaEscalada, capacidadeRestante - recargaToReserve);

                // Atualiza a bateria
                bateriaAtual += recargaEscalada;

                // Assegura que a bateria não ultrapasse o limite
                if (bateriaAtual > limiteMaximo) {
                    recargaEscalada = capacidadeRestante;
                    bateriaAtual = limiteMaximo;
                }

                System.out.printf("%-7d %-20.2f %-15.2f\n", i, recargaEscalada, bateriaAtual);
            }
        }

        System.out.println("---------------------------------------------------------------------------");
        System.out.printf("Bateria Final após recarga gradual: %.2f%%\n", bateriaAtual);
    }

    static void main(String[] ignoredArgs) {
        // Parâmetros comuns
        double limiteMaximo = 99.0;
        int etapas = 20;
        double k = 0.08;

        // Circunstância Original
        double bateriaInicialOriginal = 40.0;
        double recargaTotalOriginal = 80.0;
        atualizarBateriaGradual(bateriaInicialOriginal, recargaTotalOriginal, limiteMaximo, etapas, k);

        // Circunstância 1
        double bateriaInicial1 = 20.0;
        double recargaTotal1 = 100.0;
        atualizarBateriaGradual(bateriaInicial1, recargaTotal1, limiteMaximo, etapas, k);

        // Circunstância 2
        double bateriaInicial2 = 60.0;
        double recargaTotal2 = 50.0;
        atualizarBateriaGradual(bateriaInicial2, recargaTotal2, limiteMaximo, etapas, k);

        // Circunstância 3
        double bateriaInicial3 = 50.0;
        double recargaTotal3 = 70.0;
        atualizarBateriaGradual(bateriaInicial3, recargaTotal3, limiteMaximo, etapas, k);
    }
}
