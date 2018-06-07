package com.prista.netbanking.dao.jdbc.utils;

import java.sql.SQLException;
import java.sql.Statement;

public interface StatemenAction<RETURN_TYPE> {

    RETURN_TYPE doWithStatement(Statement stmt) throws SQLException;

}
