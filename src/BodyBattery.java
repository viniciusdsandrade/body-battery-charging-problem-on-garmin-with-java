package src;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.exp;
import static java.lang.Math.min;

public class BodyBattery {

    record StepResult(
            int etapa,
            double recargaEscalada,
            double bateriaFinal
    ) {
    }

    static List<StepResult> atualizarBateriaGradual(
            double bateriaInicial,
            double recargaTotal,
            double limiteMaximo,
            int etapas,
            double k
    ) {
        List<StepResult> resultados = new ArrayList<>();

        double bateriaAtual = bateriaInicial;
        double recargaPorEtapa = recargaTotal / etapas;
        int etapasLimite = 2; // Número de etapas permitidas para atingir o limite

        for (int i = 1; i <= etapas; i++) {
            boolean nasUltimasEtapas = (i > (etapas - etapasLimite));
            double recargaEscalada;

            if (nasUltimasEtapas) {
                if (i == etapas - 1) {
                    // Etapa penúltima: aplicar metade da recarga restante
                    double recargaRestante = limiteMaximo - bateriaAtual;
                    recargaEscalada = recargaRestante / etapasLimite;

                    // Garante que a recarga não ultrapasse o restante
                    recargaEscalada = min(recargaEscalada, recargaRestante);

                    // Atualiza a bateria

                    // Assegura que a bateria não ultrapasse o limite
                } else {
                    // Última etapa: aplicar a recarga restante para atingir o limite
                    recargaEscalada = limiteMaximo - bateriaAtual;

                    // Garante que a recarga não ultrapasse o restante
                    recargaEscalada = min(recargaEscalada, limiteMaximo - bateriaAtual);
                }
                bateriaAtual += recargaEscalada;
                if (bateriaAtual > limiteMaximo) {
                    recargaEscalada = limiteMaximo - (bateriaAtual - recargaEscalada);
                    bateriaAtual = limiteMaximo;
                }
            } else {
                // Etapas 1 a E-2: aplicar a função exponencial para escalar a recarga
                double capacidadeRestante = limiteMaximo - bateriaAtual;

                // Calcula a recarga escalada usando a função exponencial
                recargaEscalada = capacidadeRestante * (1 - exp(-k * recargaPorEtapa));

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
            }

            // Adiciona o resultado de cada etapa na lista
            resultados.add(new StepResult(i,
                    /* recargaEscalada = */ recargaEscalada,
                    /* bateriaFinal     = */ bateriaAtual)
            );
        }

        return resultados;
    }

    /**
     * Função opcional (somente para demonstração, pode ficar fora da classe BodyBattery)
     * que recebe a lista de resultados e imprime as informações no formato desejado.
     */
    static void exibirResultadoSimulacao(
            double bateriaInicial,
            double recargaTotal,
            double limiteMaximo,
            int etapas,
            double k,
            List<StepResult> resultados
    ) {
        double recargaPorEtapa = recargaTotal / etapas;
        System.out.println("\n=== Simulação ===");
        System.out.printf("Bateria Inicial: %.2f%%\n", bateriaInicial);
        System.out.printf("Recarga Total: %.2f pontos divididos em %d etapas de %.2f pontos cada\n",
                recargaTotal, etapas, recargaPorEtapa);
        System.out.println("---------------------------------------------------------------------------");
        System.out.printf("%-7s %-20s %-15s\n", "Etapa", "Recarga Escalada", "Bateria Final");
        System.out.println("---------------------------------------------------------------------------");

        // Imprime dados de cada etapa
        for (StepResult step : resultados) {
            System.out.printf("%-7d %-20.2f %-15.2f\n",
                    step.etapa(),
                    step.recargaEscalada(),
                    step.bateriaFinal());
        }

        System.out.println("---------------------------------------------------------------------------");

        // Último valor de bateria após a última etapa
        double bateriaFinal = resultados.getLast().bateriaFinal();
        System.out.printf("Bateria Final após recarga gradual: %.2f%%\n", bateriaFinal);
    }

    static void main(String[] ignoredArgs) {
        // Parâmetros comuns
        double limiteMaximo = 99.99;
        int etapas = 20;
        double k = 0.1;

        // Circunstância Original
        double bateriaInicialOriginal = 40.0;
        double recargaTotalOriginal = 80.0;

        List<StepResult> resultadosOriginal = atualizarBateriaGradual(
                bateriaInicialOriginal,
                recargaTotalOriginal,
                limiteMaximo,
                etapas,
                k
        );
        exibirResultadoSimulacao(
                bateriaInicialOriginal,
                recargaTotalOriginal,
                limiteMaximo,
                etapas,
                k,
                resultadosOriginal
        );

        // Circunstância 1
        double bateriaInicial1 = 20.0;
        double recargaTotal1 = 100.0;
        List<StepResult> resultados1 = atualizarBateriaGradual(
                bateriaInicial1,
                recargaTotal1,
                limiteMaximo,
                etapas,
                k
        );
        exibirResultadoSimulacao(
                bateriaInicial1,
                recargaTotal1,
                limiteMaximo,
                etapas,
                k,
                resultados1
        );

        // Circunstância 2
        double bateriaInicial2 = 60.0;
        double recargaTotal2 = 50.0;
        List<StepResult> resultados2 = atualizarBateriaGradual(
                bateriaInicial2,
                recargaTotal2,
                limiteMaximo,
                etapas,
                k
        );
        exibirResultadoSimulacao(
                bateriaInicial1,
                recargaTotal2,
                limiteMaximo,
                etapas,
                k,
                resultados2
        );

        // Circunstância 3
        double bateriaInicial3 = 50.0;
        double recargaTotal3 = 70.0;
        List<StepResult> resultados3 = atualizarBateriaGradual(
                bateriaInicial3,
                recargaTotal3,
                limiteMaximo,
                etapas,
                k
        );
        exibirResultadoSimulacao(
                bateriaInicial3,
                recargaTotal3,
                limiteMaximo,
                etapas,
                k,
                resultados3
        );
    }
}
