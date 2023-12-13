package manipuladorDeImagens;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * Classe para manipulação de imagens
 * 
 * @author julio.koepsel@gmail.com
 */
public class ManipuladorImagem implements ImagemPrototype {

    private BufferedImage imagem;

    /**
     * Construtor privado da classe chamado somente pelo método clone, ou pelo Builder
     * 
     * @param imagem Imagem que será manipulada
     */
    private ManipuladorImagem(BufferedImage imagem) {
        this.imagem = imagem;
    }

    /**
     * Armazena a imagem no disco
     * 
     * @param caminhoImagem Caminho e nome da imagem a ser armazenada
     * @param tipo Tipo do arquivo da imagem (.jpg, .png, etc...)
     * @throws IOException
     */
    public void saveImage(String caminhoImagem, String tipo) throws IOException {
        File file = new File(caminhoImagem);
        ImageIO.write(imagem, tipo, file);
    }

    /**
     * Retorna a imagem da classe
     */
    @Override
    public BufferedImage getImagem() {
        return imagem;
    }

    /**
     * Realiza o clone do objeto para um Prototype
     */
    @Override
    public ImagemPrototype clone() {
        return new ManipuladorImagem(imagem);
    }

    /**
     * Classe aninhada Builder, responsável pela manipulação da imagem.
     * É necessário utilizar um dos métodos load para carregar a imagem antes de usar os métodos de manipulação.
     * Os métodos de manipulação podem ser utilizados em qualquer ordem.
     * Por fim, chama-se o método build para criar o objeto manipulador com a imagem pronta.
     */
    public static class Builder {
        private BufferedImage imagem;

        /**
         * Carrega a imagem do armazenamento local
         * 
         * @param caminhoImagem Caminho e nome da imagem a ser manipulada
         * @return Builder
         * @throws IOException
         */
        public Builder load(String caminhoImagem) throws IOException {
            File file = new File(caminhoImagem);
            this.imagem = ImageIO.read(file);
            return this;
        }
        /**
         * Carrega uma imagem que já foi transformada em BufferedImage
         * 
         * @param imagem Imagem já transformada em BufferedImage
         * @return Builder
         * @throws IOException
         */
        public Builder load(BufferedImage imagem) throws IOException {
            this.imagem = imagem;
            return this;
        }
        /**
         * Carrega uma imagem de um Prototype
         * 
         * @param prototype Prototype de uma imagem que pode ser utilizada como base para outras manipulações
         * @return Builder
         * @throws IOException
         */
        public Builder load(ImagemPrototype prototype) throws IOException {
            this.imagem = prototype.getImagem();
            return this;
        }

        /**
         * Aplica um filtro cinza à imagem
         * 
         * @return Builder
         */
        public Builder applyGrayscaleFilter() {
            int width = imagem.getWidth();
            int height = imagem.getHeight();

            BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);

            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    int rgb = imagem.getRGB(i, j);

                    int r = (rgb >> 16) & 0xFF;
                    int g = (rgb >> 8) & 0xFF;
                    int b = rgb & 0xFF;
                    int gray = (int) (0.2126 * r + 0.7152 * g + 0.0722 * b);

                    int grayRGB = (gray << 16) | (gray << 8) | gray;
                    result.setRGB(i, j, grayRGB);
                }
            }

            this.imagem = result;
            return this;
        }
        /**
         * Inverte as cores da imagem
         * 
         * @return Builder
         */
        public Builder invertColors() {
            int width = imagem.getWidth();
            int height = imagem.getHeight();

            BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    int rgb = imagem.getRGB(i, j);

                    int r = 255 - (rgb >> 16) & 0xFF;
                    int g = 255 - (rgb >> 8) & 0xFF;
                    int b = 255 - rgb & 0xFF;

                    int invertedRGB = (r << 16) | (g << 8) | b;
                    result.setRGB(i, j, invertedRGB);
                }
            }

            this.imagem = result;
            return this;
        }
        /**
         * Altera o tamanho da imagem de acordo com os novos valores de largura e altura
         * 
         * @param newWidth Nova largura
         * @param newHeight Nova altura
         * @return Builder
         */
        public Builder resize(int newWidth, int newHeight) {
            BufferedImage result = new BufferedImage(newWidth, newHeight, imagem.getType());
            result.createGraphics().drawImage(imagem, 0, 0, newWidth, newHeight, null);
            this.imagem = result;
            return this;
        }
        /**
         * Rotaciona a imagem de acordo um um ângulo
         * 
         * @param degrees Ângulo para rotacionar a imagem
         * @return Builder
         */
        public Builder rotate(double degrees) {
            int width = imagem.getWidth();
            int height = imagem.getHeight();

            double radians = Math.toRadians(degrees);

            int newWidth = (int) Math.ceil(Math.abs(width * Math.cos(radians)) + Math.abs(height * Math.sin(radians)));
            int newHeight = (int) Math.ceil(Math.abs(height * Math.cos(radians)) + Math.abs(width * Math.sin(radians)));

            BufferedImage result = new BufferedImage(newWidth, newHeight, imagem.getType());

            Graphics2D g2d = result.createGraphics();
            g2d.rotate(radians, newWidth / 2.0, newHeight / 2.0);
            g2d.drawImage(imagem, (newWidth - width) / 2, (newHeight - height) / 2, null);
            g2d.dispose();

            this.imagem = result;
            return this;
        }
        /**
         * Inverte horizontalmente a imagem
         * 
         * @return Builder
         */
        public Builder flipHorizontally() {
            int width = imagem.getWidth();
            int height = imagem.getHeight();

            BufferedImage result = new BufferedImage(width, height, imagem.getType());

            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    int rgb = imagem.getRGB(i, j);
                    result.setRGB(width - i - 1, j, rgb);
                }
            }

            this.imagem = result;
            return this;
        }
        /**
         * Inverte verticalmente a imagem
         * 
         * @return Builder
         */
        public Builder flipVertically() {
            int width = imagem.getWidth();
            int height = imagem.getHeight();

            BufferedImage result = new BufferedImage(width, height, imagem.getType());

            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    result.setRGB(i, j, imagem.getRGB(i, height - 1 - j));
                }
            }

            this.imagem = result;
            return this;
        }

        /**
         * Retorna o objeto manipulador com a imagem pronta
         */
        public ManipuladorImagem build() {
            return new ManipuladorImagem(imagem);
        }
    }
}
