package commands;

import main.ServerCommandReader;

public class CommandLogout extends Command{
    public CommandLogout(String des) {
        setDescription(des);
    }
    @Override
    public String execute(ServerCommandReader caller) {
        logout(caller);
        return "Logout successfully\1";
    }

}
