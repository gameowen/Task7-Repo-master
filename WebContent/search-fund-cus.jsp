<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Mutual Fund Management</title>
<link rel="stylesheet" type="text/css" href="style/main.css">
<link rel="stylesheet" type="text/css" href="style/table.css">
<script type="text/javascript" src="scripts/submit.js"></script>
</head>
<body>
	<div id="container">
		<jsp:include page="template-header-navigation.jsp" />
		<div id="content-container">
			<jsp:include page="template-section-navigation-cus.jsp" />

			<div class="content">
				<h2>Mutual Fund Search</h2>

				<jsp:include page="error-list.jsp" />

				<form action="">
					<input class="searchinput" type="text" name="fundid"
						placeholder="Search by Fund_ID, Fund_Name or Fund_Symbol"
						onkeyup="showFund(this.value)" onchange="showFund(this.value)" />
				</form>
				<br />
				<table class='tableone'>
					<thead>
						<tr>
							<th class="th1">ID</th>
							<th class="th2">Fund Name</th>
							<th class="th3">Fund Symbol</th>
							<th class="th4">Date</th>
							<th class="th5">Price</th>
						</tr>
					</thead>
					<tfoot>
						<tr>
							<td colspan="5">Click ROW to view details for each fund.</td>
						</tr>
					</tfoot>
					<tbody>
						<tr>
							<td colspan="5">
								<div class="innerb">
									<table class="tabletwo" id="txtHint">

									</table>
								</div>
							</td>
						</tr>
					</tbody>

				</table>
			</div>
			<jsp:include page="template-footer.jsp" />
		</div>
	</div>
</body>
</html>
