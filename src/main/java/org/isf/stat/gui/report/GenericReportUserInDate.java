/*
 * Open Hospital (www.open-hospital.org)
 * Copyright © 2006-2020 Informatici Senza Frontiere (info@informaticisenzafrontiere.org)
 *
 * Open Hospital is a free and open source software for healthcare data management.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * https://www.gnu.org/licenses/gpl-3.0-standalone.html
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.isf.stat.gui.report;

import java.util.Locale;

import javax.swing.JOptionPane;

import org.isf.generaldata.GeneralData;
import org.isf.generaldata.MessageBundle;
import org.isf.menu.manager.Context;
import org.isf.serviceprinting.manager.PrintReceipt;
import org.isf.stat.dto.JasperReportResultDto;
import org.isf.stat.manager.JasperReportsManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.jasperreports.view.JasperViewer;

/*--------------------------------------------------------
 * GenericReportUserInDate
 *  - lancia un particolare report sull'utente
 * 	- la classe prevede l'inizializzazione attraverso 
 *    dadata, adata, utente, nome del report (senza .jasper)
 *---------------------------------------------------------
 * modification history
 * 06/12/2011 - prima versione
 *
 *-----------------------------------------------------------------*/
	public class GenericReportUserInDate {

    private final Logger logger = LoggerFactory.getLogger(GenericReportUserInDate.class);
	private JasperReportsManager jasperReportsManager = Context.getApplicationContext().getBean(JasperReportsManager.class);

		public GenericReportUserInDate(String fromDate, String toDate, String aUser, String jasperFileName) {
			new GenericReportUserInDate(fromDate, toDate, aUser, jasperFileName, true, true);
		}

		public  GenericReportUserInDate(String fromDate, String toDate, String aUser, String jasperFileName, boolean show, boolean askForPrint) {
			try{
                JasperReportResultDto jasperReportResultDto = jasperReportsManager.getGenericReportUserInDatePdf(fromDate, toDate, aUser, jasperFileName);
				if (show) {
                    if (GeneralData.INTERNALVIEWER) {
                        JasperViewer.viewReport(jasperReportResultDto.getJasperPrint(), false, new Locale(GeneralData.LANGUAGE));
                    } else {
                        Runtime rt = Runtime.getRuntime();
                        rt.exec(GeneralData.VIEWER + " " + jasperReportResultDto.getFilename());
                    }
				}
				
				if (GeneralData.RECEIPTPRINTER) {
                    JasperReportResultDto jasperReportTxtResultDto = jasperReportsManager.getGenericReportUserInDateTxt(fromDate, toDate, aUser, jasperFileName);
					int print = JOptionPane.OK_OPTION;
					if (askForPrint) {
						print = JOptionPane.showConfirmDialog(null, MessageBundle.getMessage("angal.genericreportbill.doyouwanttoprintreceipt"));
					}
					if (print != JOptionPane.OK_OPTION) return; //STOP
						
					new PrintReceipt(jasperReportTxtResultDto.getJasperPrint(), jasperReportTxtResultDto.getFilename());
				}
			} catch (Exception e) {
                logger.error("", e);
                JOptionPane.showMessageDialog(null, MessageBundle.getMessage("angal.stat.reporterror"), MessageBundle.getMessage("angal.hospital"), JOptionPane.ERROR_MESSAGE);
			}
		}
	}
