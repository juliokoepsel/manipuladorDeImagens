package manipuladorDeImagens;

import java.awt.image.BufferedImage;

/**
 * Interface para a geração de um Prototype
 * 
 * @author julio.koepsel@gmail.com
 */
public interface ImagemPrototype {

    /**
     * Retorna um clone do objeto
     */
    public ImagemPrototype clone();

    /**
     * Retorna um a imagem do objeto
     */
    public BufferedImage getImagem();

}