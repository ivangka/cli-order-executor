package ivangka.cliorderexecutor.runner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ivangka.cliorderexecutor.controller.TerminalController;

@Component
public class TerminalRunner implements CommandLineRunner {

    private final TerminalController terminalController;

    @Autowired
    public TerminalRunner(TerminalController terminalController) {
        this.terminalController = terminalController;
    }

    @Override
    public void run(String... args) {
        terminalController.controller();
    }

}
