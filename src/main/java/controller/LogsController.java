package controller;

import repository.LogsRepository;
import tables.Logs;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

public class LogsController {
    private LogsRepository logsRepository;

    public LogsController() {
        getLogsRepository();
    }

    private LogsRepository getLogsRepository() {
        if (Objects.isNull(logsRepository)) {
            logsRepository = new LogsRepository();
        }
        return logsRepository;
    }

    public List<Logs> getLogs() throws SQLException {
        List<Logs> logsList = logsRepository.getLogs();
        return logsList;
    }
}
