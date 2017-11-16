# CreditTimetableJSF
Generator of Credit Timetable using JSF
Application is available on website: http://timetable.eu-west-1.elasticbeanstalk.com.

The application collects offers of housing loans from the https://www.bankier.pl/kredyty-hipoteczne/porownaj-oferty using the jsoup library.
The user has the option of selecting a home loan and generating a loan schedule using the installment plan. Credit installment is calculated based on the value entered by the user. The interest rate is the sum of the margin of the selected loan offer and the fixed 3M WIBOR: 1.73%. The user has the option to export the loan repayment schedule to an Excel or PDF file.

Tools and technologies used for the application:
<li>Java
<li>JavaServer Faces
<li>Primefaces
<li>jsoup.org
<li>poi.apache.org
<li>itextpdf.com
