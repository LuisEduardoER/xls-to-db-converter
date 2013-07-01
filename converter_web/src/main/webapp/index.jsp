<html>
	<body>
		<ul>
		<%
			String temp = "";
			for (int i=0; i<5; i++) {
				if (i%2 == 0) {
					temp = "even";
				}
				else {
					temp = "odd";
				}
		%>
			<li><%= i %> is an <%= temp %>number</li>
		<%
			}
		%>
		</ul>
		&copy
	</body>
</html>