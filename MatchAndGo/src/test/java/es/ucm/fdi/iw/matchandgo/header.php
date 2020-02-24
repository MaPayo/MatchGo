<!-- HEADER HTML -->
<header>
<nav class="navbar navbar-expand-lg">
<div>
<div class="inlineblock">
<a class="navbar-brand" href="/profile.php">
<picture>
	<source type="image/webp" srcset="/media/logolarge.webp">
	<img src="/media/logolarge.png" title="Inicio" alt="" class="img200" />
</picture>
</a>
</div>
</div>
<div class="inlineblock verticalalign">
<button class="navbar-toggler floatrigth second-button" type="button" data-toggle="collapse" data-target="#navbarSupportedContent23" aria-controls="navbarSupportedContent23" aria-expanded="false" aria-label="Toggle navigation">
<div class="animated-icon2"><span></span><span></span><span></span><span></span></div> 
</button>
</div>
  <div class="collapse navbar-collapse width100" id="navbarSupportedContent23">
<?php if (!isset($_SESSION["id"])){?>	
<div class="dropdown">
  <button class="btn btn-secondary dropdown-toggle" type="button" id="dropdownMenuButton" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
	    Administrar Empresas
  </button>
  <div class="dropdown-menu width100" aria-labelledby="dropdownMenuButton">
    <a class="dropdown-item" href="/addbusiness.php">Registrar Empresa</a>
    <a class="dropdown-item" href="/business.php">Modificar Empresa</a>
  </div>
</div>

<div class="dropdown">
  <button class="btn btn-secondary dropdown-toggle" type="button" id="dropdownMenuButton" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
    Administrar Productos
  </button>
  <div class="dropdown-menu width100" aria-labelledby="dropdownMenuButton">
   <a class="dropdown-item" href="addarticle.php">Registrar Producto</a>
    <a class="dropdown-item" href="addstock.php">Nueva Compra</a>
    <a class="dropdown-item" href="addtranslation.php">Añadir Traducción</a>
 <!--  <a class="dropdown-item" href="listArticles.php">Listado Venta Prod.</a>-->
    <a class="dropdown-item" href="articles.php">Listado de productos</a>
  </div>
</div>

<div class="dropdown">
  <button class="btn btn-secondary dropdown-toggle" type="button" id="dropdownMenuButton" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
    Facturación
  </button>
  <div class="dropdown-menu width100" aria-labelledby="dropdownMenuButton">
   <a class="dropdown-item" href="addQuotation.php">Crear Cotización</a>
   <a class="dropdown-item" href="quotations.php">Cotizacióes</a>
  <a class="dropdown-item" href="addBudget.php">Crear Presupuesto</a>
    <a class="dropdown-item" href="budgets.php">Presupuestos</a>
    <a class="dropdown-item" href="proInvoices.php">Facturas Proforma</a>
    <a class="dropdown-item" href="invoices.php">Facturas</a>
  <!-- <a class="dropdown-item" href="#">Factura Proforma</a>
    <a class="dropdown-item" href="#">Factura</a>
    <a class="dropdown-item" href="#">Albarán</a>
    <a class="dropdown-item" href="#">Balance Contable</a>-->
  </div>
</div>

<div class="dropdown">
  <button class="btn btn-secondary dropdown-toggle" type="button" id="dropdownMenuButton" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
    Otros
  </button>
  <div class="dropdown-menu width100" aria-labelledby="dropdownMenuButton">
    <a class="dropdown-item" href="warehouses.php">Almacenes</a>
    <a class="dropdown-item" href="packaging.php">Envases</a>
    <a class="dropdown-item" href="languages.php">Idiomas</a>
    <a class="dropdown-item" href="families.php">Familias</a>
    <a class="dropdown-item" href="/addPersonalInfo.php">Empresas Fact.</a>
    <a class="dropdown-item" href="/users.php">Usuarios</a>
    <a class="dropdown-item" href="/setting.php">Configuración</a>
<!--<a class="dropdown-item" href="#">Cuentas Contables</a>-->
  </div>
	<?php }?>
  <!-- <form class="form-inline my-2 my-lg-0">
      <input class="form-control mr-sm-2" type="search" placeholder="Search" aria-label="Search">
      <button class="btn btn-outline-success my-2 my-sm-0" type="submit">Search</button>
    </form>-->

</div>

<div class="nav-item talignm">
<a href="/controller/logout_controller.php"><img src="/media/logout.png" title="Cerrar Sesion" alt="Cerrar Sesion" width="25px" height="25px"></a>
</div>  
</nav>
<script nonce=lhCaYHI2S2zxAK3H3Wnu>
$(document).ready(function () {

	  $('.first-button').on('click', function () {

		      $('.animated-icon1').toggleClass('open');
		        });
	    $('.second-button').on('click', function () {

		        $('.animated-icon2').toggleClass('open');
			  });
	    $('.third-button').on('click', function () {

		        $('.animated-icon3').toggleClass('open');
			  });
});
</script>
<script nonce=lhCaYHI2S2zxAK3H3Wnu>
$('.dropdown').on('show.bs.dropdown', function() {
	$(this).find('.dropdown-menu').first().stop(true, true).slideDown();
});
$('.dropdown').on('hide.bs.dropdown', function() {
	$(this).find('.dropdown-menu').first().stop(true, true).slideUp();
});
</script>
</header>

