package net.wiicart.karatasi.screen.helper;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.SimpleTheme;
import com.googlecode.lanterna.gui2.Borders;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.Direction;
import com.googlecode.lanterna.gui2.LinearLayout;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.TextBox;
import net.wiicart.karatasi.screen.PrimaryScreen;
import org.jetbrains.annotations.NotNull;

public final class ToolBar {

    private final @NotNull PrimaryScreen screen;

    private final @NotNull String title;

    private final @NotNull Panel panel;

    private final @NotNull TextBox addressBox;

    public ToolBar(@NotNull PrimaryScreen screen, @NotNull String title, @NotNull String address) {
        this.screen = screen;
        this.title = title;
        panel = new Panel();
        addressBox = new TextBox(address);
        addressBox.setSize(new TerminalSize(screen.getColumnCount() - 10, 1));
        addressBox.setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Fill));
        initPanel();
    }


    private void initPanel() {
        setPanelSettings();
        panel.addComponent(new Button("←", screen::backwards));
        panel.addComponent(new Button("→", screen::forwards));
        panel.addComponent(new Button("⟳", screen::refresh));
        panel.addComponent(addressBox);
        panel.addComponent(new Button("Go", () -> screen.goToAddress(addressBox.getText(), true)));
        panel.addComponent(new Button("Exit", screen::exit));
    }

    private void setPanelSettings() {
        panel.setTheme(new SimpleTheme(TextColor.ANSI.BLUE, TextColor.ANSI.WHITE));
        panel.setLayoutManager(new LinearLayout(Direction.HORIZONTAL));
        panel.setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Fill));
        panel.setPosition(new TerminalPosition(0, 0)); // ToolBar must stick to the top
        panel.withBorder(Borders.singleLine(title));
    }

    public @NotNull Panel getPanel() {
        return panel;
    }

    public void setPanelSize(int columns) {
        panel.setSize(new TerminalSize(columns, 1));
    }

    public void setAddress(@NotNull String address) {
        addressBox.setText(address);
    }

}
