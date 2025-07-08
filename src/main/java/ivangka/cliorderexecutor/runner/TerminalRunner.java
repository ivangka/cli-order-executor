package ivangka.cliorderexecutor.runner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ivangka.cliorderexecutor.cli.CliHandler;

@Component
public class TerminalRunner implements CommandLineRunner {

    private final CliHandler cliHandler;

    @Autowired
    public TerminalRunner(CliHandler cliHandler) {
        this.cliHandler = cliHandler;
    }

    @Override
    public void run(String... args) {
        cliHandler.start();
    }

}
