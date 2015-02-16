<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!Doctype html>
<html itemscope itemtype="http://schema.org/WebPage">
    <head>
        <meta charset="utf-8">
        <title>All ${bank} Banks ${state} IFSC codes | ifsc code|Banks Ifsc code</title>
        <meta name="description" content="All ${bank} Banks ${state} ifsc codes,ifsc,ifsc codes">
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
                <h1 class='white'  itemprop="headline" >All ${bank}   Banks in ${state} Ifsc Codes.</h1>
            </div>
        </nav>
        <div class='col-lg-1 hidden-md hidden-sm hidden-xs'>
        </div>
        <div class='col-lg-10 col-md-12 col-sm-12 col-xs-12'>
            <div class="panel panel-default margin-top">
                <div class="panel-heading">
                    <h2 itemprop='breadcrumb' ><a href="/#!${bank}"> ${bank}  Bank > </a> <a href="/#!${bank}/${state}">${state} ></a>   District  List</h2>
                </div>
                <div class="panel-body">
                    <c:forEach items="${districts}" var="district">
                    <div class='col-lg-4' itemprop='about' itemscope itemtype='http://schema.org/Thing' >
                        <h3 itemprop='name'><a href='/#!${bank}/${state}/${district}'>${district}</a></h3>
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