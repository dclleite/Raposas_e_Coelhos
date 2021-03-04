import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.HashMap;

/**
 * Uma visão gráfica da classe de simulação.
 * A visualização mostra um retângulo colorido para cada local
 * representado seu conteúdo. Ele usa uma cor de fundo padrão.
 * Conforme núcleos para cada tipo de espécie podem ser definidas com base no
 * Método setColor.
 *
 * @author David J. Barnes e Michael Kolling
 * @version 2002-04-23 
 */
public class SimulatorView extends JFrame {
    // Cores usadas para locais vazios. 
    private static final Color EMPTY_COLOR = Color.white;

    // Cor usada para objetos que não têm cor definida. 
    private static final Color UNKNOWN_COLOR = Color.gray;

    private final String STEP_PREFIX = "Step: ";
    private final String POPULATION_PREFIX = "Population: ";
    private JLabel stepLabel, population;
    private FieldView fieldView;

    // Um mapa para armazenar cores para os participantes da simulação 
    private HashMap colors;
    // Um objeto de estatística computando e armazenando informações de simulação 
    private FieldStats stats;

    /**
     * Crie uma visualização da largura e altura fornecidas.
     */
    public SimulatorView(int height, int width) {
        stats = new FieldStats();
        colors = new HashMap();

        setTitle("Fox and Rabbit Simulation");
        stepLabel = new JLabel(STEP_PREFIX, JLabel.CENTER);
        population = new JLabel(POPULATION_PREFIX, JLabel.CENTER);

        setLocation(100, 50);

        fieldView = new FieldView(height, width);

        Container contents = getContentPane();
        contents.add(stepLabel, BorderLayout.NORTH);
        contents.add(fieldView, BorderLayout.CENTER);
        contents.add(population, BorderLayout.SOUTH);
        pack();
        setVisible(true);
    }

    /**
     * Defina uma cor a ser usada para uma determinada classe de animal.
     */
    public void setColor(Class animalClass, Color color) {
        colors.put(animalClass, color);
    }

    /**
     * Defina uma cor a ser usada para uma determinada classe de animal.
     */
    private Color getColor(Class animalClass) {
        Color col = (Color) colors.get(animalClass);
        if (col == null) {
            // nenhuma cor definida para esta classe 
            return UNKNOWN_COLOR;
        } else {
            return col;
        }
    }

    /**
     * Mostra o status atual do campo.
     * @param step  Qual etapa de iteração é.
     * @param stats Status do campo a ser representado.
     *
     */
    public void showStatus(int step, Field field) {
        if (!isVisible())
            setVisible(true);

        stepLabel.setText(STEP_PREFIX + step);

        stats.reset();
        fieldView.preparePaint();

        for (int row = 0; row < field.getDepth(); row++) {
            for (int col = 0; col < field.getWidth(); col++) {
                Object animal = field.getObjectAt(row, col);
                if (animal != null) {
                    stats.incrementCount(animal.getClass());
                    fieldView.drawMark(col, row, getColor(animal.getClass()));
                } else {
                    fieldView.drawMark(col, row, EMPTY_COLOR);
                }
            }
        }
        stats.countFinished();

        population.setText(POPULATION_PREFIX + stats.getPopulationDetails(field));
        fieldView.repaint();
    }

    /**
     * Determine se a simulação deve continuar em execução.
     *
     * @return true Se houver mais de uma espécie viva. 
     */
    public boolean isViable(Field field) {
        return stats.isViable(field);
    }

    /**
     * Fornece uma visualização gráfica de um campo retangular. Isto é
     * uma classe aninhada (uma classe definida dentro de uma classe) que
     * define um componente personalizado para a interface do usuário. Esta
     * componente exibe o campo.
     * Este é um material de GUI bastante avançado - você pode ignorar isso
     * para o seu projeto, se desejar.
     */
    private class FieldView extends JPanel {
        private final int GRID_VIEW_SCALING_FACTOR = 6;

        private int gridWidth, gridHeight;
        private int xScale, yScale;
        Dimension size;
        private Graphics g;
        private Image fieldImage;

        /**
         * Crie um novo componente FieldView. 
         */
        public FieldView(int height, int width) {
            gridHeight = height;
            gridWidth = width;
            size = new Dimension(0, 0);
        }

        /**
         * Diga ao gerenciador de GUI o quão grande gostaríamos de ser.
         */
        public Dimension getPreferredSize() {
            return new Dimension(gridWidth * GRID_VIEW_SCALING_FACTOR,
                gridHeight * GRID_VIEW_SCALING_FACTOR);
        }

        /**
         * Prepare-se para uma nova rodada de pintura. Já que o componente
         * pode ser redimensionado, calcule o fator de escala novamente.
         */
        public void preparePaint() {
            if (!size.equals(getSize())) {  // if the size has changed... // se o tamanho mudou ...
                size = getSize();
                fieldImage = fieldView.createImage(size.width, size.height);
                g = fieldImage.getGraphics();

                xScale = size.width / gridWidth;
                if (xScale < 1) {
                    xScale = GRID_VIEW_SCALING_FACTOR;
                }
                yScale = size.height / gridHeight;
                if (yScale < 1) {
                    yScale = GRID_VIEW_SCALING_FACTOR;
                }
            }
        }

        /**
         * Pinte no local da grade neste campo em uma determinada cor.
         */
        public void drawMark(int x, int y, Color color) {
            g.setColor(color);
            g.fillRect(x * xScale, y * yScale, xScale - 1, yScale - 1);
        }

        /**
         * O componente de visualização de campo precisa ser exibido novamente. Copie o
         * imagem interna na tela.
         */
        public void paintComponent(Graphics g) {
            if (fieldImage != null) {
                g.drawImage(fieldImage, 0, 0, null);
            }
        }
    }
}
