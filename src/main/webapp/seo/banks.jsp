<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!Doctype html>
<html>
	<head>
		<meta charset="utf-8">
        <title>All Indian Banks IFSC codes | ifsc code|Banks Ifsc code</title>
        <meta name="description" content="All Indian Banks ifsc codes,ifsc,ifsc codes">
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css">
        <style>
        .margin-top{
        	margin-top:90px;
        }
        .white{
        color:white;
        }
        </style>		
	</head>
	<body>
	<nav class="navbar navbar-inverse navbar-fixed-top">
      <div class="container">
        <h1 class='white'>All Indian Bank Ifsc Codes.</h1>        
      </div>
    </nav>
    <div class='col-lg-1 hidden-md hidden-sm hidden-xs' >
    </div>
    <div class='col-lg-10 col-md-12 col-sm-12 col-xs-12'>
    	<div class="panel panel-default margin-top">
    		<div class="panel-heading">
    			<h2>IFSC Banks List</h2>
    		</div>
  			<div class="panel-body">
  			<c:forEach items="${banksList}" var="bank">
    			<div class='col-lg-4' itemscope itemtype="http://schema.org/Organization">
    				<h3 itemprop="name"><a href='/#!${bank}'>${bank}</a></h3>
    			</div>
    		</c:forEach>	
  			</div>
		</div>
    </div>
    <div class='col-lg-1 hidden-md hidden-sm hidden-xs'>
    </div>
	</body>	
</html>