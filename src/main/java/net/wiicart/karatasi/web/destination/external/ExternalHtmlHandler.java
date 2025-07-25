package net.wiicart.karatasi.web.destination.external;

import com.googlecode.lanterna.gui2.Panel;
import net.wiicart.karatasi.config.Option;
import net.wiicart.karatasi.exception.LoadFailureException;
import net.wiicart.karatasi.screen.PrimaryScreen;
import net.wiicart.karatasi.web.destination.Destination;
import net.wiicart.karatasi.web.renderer.primitivetext.PrimitiveTextBoxRenderer;
import org.jetbrains.annotations.NotNull;
import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.UnknownHostException;

// https://htmlunit.sourceforge.io/gettingStarted.html
final class ExternalHtmlHandler implements Destination.Handler {

    private final @NotNull String address;

    private final PrimaryScreen screen;

    private final int timeout;

    private Document document;

    ExternalHtmlHandler(@NotNull String address, @NotNull PrimaryScreen screen) throws LoadFailureException {
        this.address = address;
        this.screen = screen;
        timeout = screen.getBrowser().config().getInt(Option.Int.TIMEOUT);
        load();
    }

    private void load() throws LoadFailureException {
        Connection connection = Jsoup.connect(address)
                .timeout(timeout)
                .followRedirects(true);

        try {
            document = connection.get();
        } catch(final HttpStatusException e) {
            throw new LoadFailureException(e.getStatusCode(), e.getCause());
        } catch(final UnknownHostException e) {
            throw new LoadFailureException(700, address); // 700 signifies unknown host
        } catch(final IOException e) {
            throw new LoadFailureException(-1, e);
        }
    }

    @Override
    public void applyContent(@NotNull Panel panel) {
        new PrimitiveTextBoxRenderer(document).applyContent(panel);
        // new SimpleRenderer(screen, document).applyContent(panel);
        // new PrimitiveTextBoxRenderer(document).applyContent(panel);
    }

    @Override
    public @NotNull String getTitle() {
        return document.title();
    }
}
