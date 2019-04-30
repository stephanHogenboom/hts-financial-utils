package com.hogenboom.finane.utils;

import com.hogenboom.finane.utils.parser.ing.model.FinancialRecord;

import java.io.IOException;
import java.util.List;

public interface FinancialParser {
  List<FinancialRecord> parse(String path, Boolean skipHeader) throws IOException;
}
