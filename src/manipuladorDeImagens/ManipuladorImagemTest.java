package manipuladorDeImagens;

import java.io.File;
import java.io.IOException;

import org.junit.Test;
import junit.framework.TestCase;

/**
 * Classe de Testes Unitários da classe ManipuladorImagem
 * 
 * @author julio.koepsel@gmail.com
 */
public class ManipuladorImagemTest extends TestCase {

    /**
     * Teste do método saveImage, verifica se a imagem é criada após a sua construção
     * 
     * @throws IOException
     */
    @Test
    public void testSaveImage() throws IOException {
        File file = new File("imagem_cinza_teste.jpg");
        if (file.exists()) {
            file.delete();
        }

        ManipuladorImagem imagemTeste = new ManipuladorImagem.Builder()
            .load("imagem.jpg")
            .applyGrayscaleFilter()
            .build();

        imagemTeste.saveImage("imagem_cinza_teste.jpg", "jpg");

        file = new File("imagem_cinza_teste.jpg");

        assertEquals(file.exists(), true);
    }
    
    /**
     * Teste do método testClone, verifica se a imagem é criada após sua clonagem
     * 
     * @throws IOException
     */
    @Test
    public void testClone() throws IOException {
        File file = new File("imagem_invertida_teste.jpg");
        if (file.exists()) {
            file.delete();
        }

        ManipuladorImagem imagemTeste = new ManipuladorImagem.Builder()
            .load("imagem.jpg")
            .build();

        ImagemPrototype imagemPrototypeTeste = imagemTeste.clone();

        imagemTeste = new ManipuladorImagem.Builder()
            .load(imagemPrototypeTeste)
            .invertColors()
            .build();

        imagemTeste.saveImage("imagem_invertida_teste.jpg", "jpg");

        file = new File("imagem_invertida_teste.jpg");

        assertEquals(file.exists(), true);
    }
}
