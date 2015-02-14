<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
%@ page import="java.util.List" %>
%@ page import="bnifsc.entites.Branch" %>
<!Doctype html>
<html itemscope itemtype="http://schema.org/WebPage">
  <head>
    <meta charset="utf-8">
    <title>All ${bank} Banks ${state}, ${district},${branchName}  IFSC codes | ifsc code|Banks Ifsc code</title>
    <meta name="description" content="All ${bank} Banks ${state}, ${district}, ${branchName} ifsc codes,ifsc,ifsc codes">
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
        <h1 class='white'  itemprop="headline" itemscope  itemtype="http://schema.org/Text">All ${bank}   Banks in ${state} ${district} ,${branchName} Ifsc Codes.</h1>
      </div>
    </nav>
    <div class='col-lg-1 hidden-md hidden-sm hidden-xs'>
    </div>
    <div class='col-lg-10 col-md-12 col-sm-12 col-xs-12'>
      <div class="panel panel-primary margin-top " >
        <div class="panel-heading" itemprop="mainContentOfPage" itemscope itemtype="http://schema.org/WebPageElement">
          <h3><span itemprop="about" itemscope itemtype="http://schema.org/Thing"><span itemprop="name">${branch.name} Bank</span></span>,<span itemprop="alternativeHeadline">${branch.district}, ${branch.branchName} (${branch.address})</span></h3>
        </div>
        <div class="panel-body" itemprop="contentLocation">
          <table class="table .table-hover ">
            <tr>
              <td style="min-width: 120px;"  class='text-right'><h2>IFSC :</h2></td><td><h2>${branch.ifsc}</td>
            </tr>
            <tr>
              <td class='text-right'>MICR :</td><td>${branch.micr}</td>
            </tr>
            <tr>
              <td class='text-right'>Bank Name :</td><td>${branch.name}</td>
            </tr>
            <tr>
              <td class='text-right' >Branch Name :</td><td itemprop="name">${branch.branchName}</td>
            </tr>
            <tr>
              <td class='text-right'>Address :</td><td id="address" itemprop="address" itemscope itemtype="http://schema.org/PostalAddress">${branch.address}</td>
            </tr>
            <tr>
              <td class='text-right'>Pin Code :</td><td itemref="address" itemprop="postalCode">${branch.pincode}</td>
            </tr>
            <tr>
              <td class='text-right'>District :</td><td itemref="address" itemprop="addressLocality">${branch.district}</td>
            </tr>
            <tr>
              <td class='text-right'>State :</td><td itemprop="address" itemprop="addressRegion">${branch.state}</td>
            </tr>
            <tr>
              <td class='text-right'>Phone :</td><td itemref="address" itemprop="telephone">${branch.phone}</td>
            </tr>
            <tr>
              <td class='text-right'>Email :</td><td itemref="address" itemprop="email" >${branch.email}</td>
            </tr>
            <tr>
              <td class='text-right'>Customer Care :</td><td>${branch.custCare}</td>
            </tr>
          </table>
        </div>
        <div class="panel-footer"></div>
      </div>
    </div>
    <div class='col-lg-1 hidden-md hidden-sm hidden-xs'>
    </div>
  </body>
</html>