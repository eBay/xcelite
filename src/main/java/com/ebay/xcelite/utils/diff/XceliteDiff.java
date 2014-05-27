/*
  Copyright [2013-2014] eBay Software Foundation

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package com.ebay.xcelite.utils.diff;

import java.util.Collection;

import javax.annotation.Nonnull;

import org.apache.commons.collections.CollectionUtils;

import com.ebay.xcelite.reader.SheetReader;
import com.ebay.xcelite.utils.diff.info.Collections;
import com.ebay.xcelite.utils.diff.info.Files;
import com.ebay.xcelite.utils.diff.info.Info;
import com.ebay.xcelite.utils.diff.info.Sheets;
import com.ebay.xcelite.utils.diff.report.NewLineDecorator;
import com.ebay.xcelite.utils.diff.report.ReportGenerator;
import com.ebay.xcelite.utils.diff.report.ReportInfo;

/**
 * Utility which compares two sheets and returns the difference between them.
 * 
 * @author kharel (kharel@ebay.com)
 * @creation_date Nov 1, 2013
 * 
 */
public final class XceliteDiff {

  private static final String NEW_LINE = System.getProperty("line.separator");

  private XceliteDiff() {
  }

  /**
   * Returns the difference between two sheets. Note that T must implement
   * hashCode() and equals() if you wish to have meaningful symmetric difference
   * results.
   * 
   * @param a the first sheet
   * @param b the second sheet
   * @return DiffResult object which holds the diff result
   */
  public static <T> DiffResult<T> diff(@Nonnull SheetReader<T> a, @Nonnull SheetReader<T> b) {
    return diff(a, b, null);
  }

  /**
   * Returns the difference between two sheets. Note that T must implement
   * hashCode() and equals() if you wish to have meaningful symmetric difference
   * results.
   * 
   * @param a the first sheet
   * @param b the second sheet
   * @param reportGenerator a custom reporter implementation
   * @return DiffResult object which holds the diff result
   */
  @SuppressWarnings("unchecked")
  public static <T> DiffResult<T> diff(@Nonnull SheetReader<T> a, @Nonnull SheetReader<T> b,
      ReportGenerator reportGenerator) {
    Collection<T> ca = a.read();
    Collection<T> cb = b.read();
    Collection<T> disjunction = CollectionUtils.disjunction(ca, cb);
    Info<T> info = new ReportInfo<T>(new Files(a.getSheet().getFile().getAbsolutePath(), b.getSheet().getFile()
        .getAbsolutePath()), new Sheets(a.getSheet().getNativeSheet().getSheetName(), b.getSheet().getNativeSheet()
        .getSheetName()), new Collections<T>(ca, cb, disjunction));
    ReportGenerator reporter;
    if (reportGenerator != null) {
      reporter = reportGenerator;
    } else {
      reporter = new SimpleReportGenerator();
    }
    return new DiffResultImpl<T>(disjunction, reporter.generateReport(info));
  }

  private static class DiffResultImpl<T> implements DiffResult<T> {

    private final Collection<T> diff;
    private final boolean isIdentical;
    private final String report;

    public DiffResultImpl(Collection<T> diff, String report) {
      this.diff = diff;
      this.report = report;
      isIdentical = diff.size() == 0;
    }

    @Override
    public boolean isIdentical() {
      return isIdentical;
    }

    @Override
    public Collection<T> getDifference() {
      return diff;
    }

    @Override
    public String getReport() {
      return report;
    }
  }

  private static class SimpleReportGenerator implements ReportGenerator {
    @Override
    public <T> String generateReport(Info<T> info) {
      StringBuilder sb = new StringBuilder();
      sb.append("File " + info.files().aFile() + ", ");
      sb.append("Sheet: " + info.sheets().aSheetname() + ", ");
      sb.append(String.format("items (%s):" + NEW_LINE, info.collections().a().size()));
      sb.append(NEW_LINE);
      sb.append(new NewLineDecorator<T>(info.collections().a()));
      sb.append(NEW_LINE);

      sb.append("File " + info.files().bFile() + ", ");
      sb.append("Sheet: " + info.sheets().bSheetname() + ", ");
      sb.append(String.format("items (%s):" + NEW_LINE, info.collections().b().size()));
      sb.append(NEW_LINE);
      sb.append(new NewLineDecorator<T>(info.collections().b()));
      sb.append(NEW_LINE);

      sb.append(String.format("Difference (%s):" + NEW_LINE, info.collections().difference().size()));
      sb.append(NEW_LINE);
      sb.append(new NewLineDecorator<T>(info.collections().difference()));
      return sb.toString();
    }
  }
}
