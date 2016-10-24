package com.crossover.trial.weather;

import static java.lang.String.format;

import java.io.IOException;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.glassfish.grizzly.Connection;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.HttpServerFilter;
import org.glassfish.grizzly.http.server.HttpServerProbe;
import org.glassfish.grizzly.http.server.Request;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

/**
 * This main method will be use by the automated functional grader. You
 * shouldn't move this class or remove the main method. You may change the
 * implementation, but we encourage caution.
 *
 * @author code test administrator
 */
public class WeatherServer {

    /**
     * Log of the Class.
     */
    public static final Logger LOGGER = Logger
            .getLogger(WeatherClient.class.getName());

    /**
     * Base URL for The Server.
     */
    private static final String BASE_URL = "http://localhost:9090/";

    /**
     * Main Method.
     * 
     * @param args
     */
    public static void main(String[] args) {
        try {            
            LOGGER.log(Level.INFO,"Starting Weather App local testing server: " + BASE_URL);

            final ResourceConfig resourceConfig = new ResourceConfig();
            resourceConfig.register(RestWeatherCollectorEndpoint.class);
            resourceConfig.register(RestWeatherQueryEndpoint.class);

            HttpServer server = GrizzlyHttpServerFactory.createHttpServer(
                    URI.create(BASE_URL), resourceConfig, false);
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                server.shutdownNow();
            }));

            HttpServerProbe probe = new HttpServerProbe.Adapter() {
                public void onRequestReceiveEvent(final HttpServerFilter filter,
                        final Connection connection, final Request request) {
                    LOGGER.log(Level.INFO,request.getRequestURI());
                }
            };
            server.getServerConfiguration().getMonitoringConfig()
                    .getWebServerConfig().addProbes(probe);

            // the autograder waits for this output before running automated
            // tests, please don't remove it
            server.start();
            
            LOGGER.log(Level.INFO,format("Weather Server started.\n url=%s\n", BASE_URL));
            
//            System.out.println(
//                    format("Weather Server started.\n url=%s\n", BASE_URL));

            // blocks until the process is terminated
            Thread.currentThread().join();
            server.shutdown();
        } catch (IOException | InterruptedException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }
}
