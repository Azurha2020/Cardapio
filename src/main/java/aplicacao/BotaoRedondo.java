package aplicacao;
import javax.swing.*;
import java.awt.*;

public class BotaoRedondo extends JButton {
    
    public BotaoRedondo(String label) {
        super(label);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
    }

   @Override
protected void paintComponent(Graphics g) {
    Graphics2D g2 = (Graphics2D) g.create();
    // Ativa o antialiasing para máxima nitidez
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    
    // 1. Desenha o fundo circular
    if (getModel().isArmed()) {
        g2.setColor(getBackground().darker());
    } else {
        g2.setColor(getBackground());
    }
    g2.fill(new java.awt.geom.Ellipse2D.Double(0, 0, getWidth() - 1, getHeight() - 1));
    
    // 2. Desenha o texto usando a geometria real do caractere
    String texto = getText();
    if (texto != null && !texto.isEmpty()) {
        g2.setFont(getFont());
        g2.setColor(getForeground());
        
        // Obtém o desenho físico exato do caractere
        java.awt.font.FontRenderContext frc = g2.getFontRenderContext();
        java.awt.font.TextLayout layout = new java.awt.font.TextLayout(texto, getFont(), frc);
        java.awt.geom.Rectangle2D limites = layout.getBounds();
        
        // Cálculo do centro absoluto baseado no desenho real do "+"
        float x = (float) ((getWidth() - limites.getWidth()) / 2.0 - limites.getX());
        float y = (float) ((getHeight() - limites.getHeight()) / 2.0 - limites.getY());
        
        g2.drawString(texto, x, y);
    }
    
    g2.dispose();
}

}
