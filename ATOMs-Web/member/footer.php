

    <script src="../js/jquery-1.11.1.min.js"></script>
    <script src="../js/bootstrap.min.js"></script>
    <script src="../js/slick.min.js"></script>
    <script src="../js/placeholdem.min.js"></script>
    <script src="../js/rs-plugin/js/jquery.themepunch.plugins.min.js"></script>
    <script src="../js/rs-plugin/js/jquery.themepunch.revolution.min.js"></script>
    <script src="../js/waypoints.min.js"></script>
    <script src="../js/scripts.js"></script>

    <script>
        $(document).ready(function() {
            appMaster.preLoader();
        });
        $(function(){
          $('body').css({ height: $(window).innerHeight() });
          $(window).resize(function(){
            $('body').css({ height: $(window).innerHeight() });
          });
        });
    </script>


</body>

</html>
