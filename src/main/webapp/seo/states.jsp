<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!Doctype html>
<html lang="en" itemscope itemtype="http://schema.org/WebPage">
    <head>
        <meta charset="utf-8">
        <title>All ${bank} Banks IFSC codes | ${bank} ifsc code| ${bank} Banks Ifsc code</title>
        <meta name="description" content="All ${bank} Banks ifsc codes,ifsc,ifsc codes">
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
    <body >
        <nav class="navbar navbar-inverse navbar-fixed-top">
            <div class="container" >
                <h1 class='white'  ">All ${bank}  Banks Ifsc Codes.</h1>
            </div>
        </nav>
        <div class='col-lg-1 hidden-md hidden-sm hidden-xs'>
        </div>
        <div class='col-lg-10 col-md-12 col-sm-12 col-xs-12'>
            <div class="panel panel-default margin-top">
                <div class="panel-heading">
                    <h2 itemprop='breadcrumb' ><a href="#!${bank}"> ${bank} </a> > Bank State  List</h2>
                </div>
                <div class="panel-body">
                    <c:forEach items="${states}" var="state">
                    <div class='col-lg-4' itemprop='about' itemscope itemtype='http://schema.org/Thing' >
                        <h3 itemprop='name'><a href='/#!${bank}/${state}'>${state}</a></h3>
                    </div>
                    </c:forEach>
                </div>
                <div class="panel-footer">
                    <div class='col-lg-3' itemprop='creator' itemscope itemtype="http://schema.org/Person">
                        <label itemprop='name'>vinaymavi</label>
                        <label itemprop='email'>vinaymavi@gamil.com</label>
                    </div>
                </div>
            </div>
        </div>
        <div class='col-lg-1 hidden-md hidden-sm hidden-xs'>
        </div>
    </body>
</html>