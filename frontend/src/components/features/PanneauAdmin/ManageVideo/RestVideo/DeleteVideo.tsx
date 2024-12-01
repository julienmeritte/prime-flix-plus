import { useState } from "react";
import { Alert, Button } from "react-bootstrap";
import * as videoService from "services/VideoService";

interface DeleteVideoProps {
    id: number;
  }

function DeleteVideo(props: DeleteVideoProps) {
    const [ready, setReady] = useState(false);

    const deleteVideoForm = async () => {
        const fetchedDeleteVideoForm = await videoService.deleteVideo(props.id);
        if (fetchedDeleteVideoForm.data && fetchedDeleteVideoForm.status === 201) {
          console.log(fetchedDeleteVideoForm.data);
          setReady(true);
        }
      };
    
      function handleClickSuppression() {
        deleteVideoForm();
      }


    return (
        <>
          {ready ? (
            <div>Supprimé avec succès</div>
          ) : (
            <>
              <Alert variant="danger" className="surcharge-alert-danger">
                <Alert.Heading>Attention</Alert.Heading>
                <p>
                  Vous êtes sur le point de détruire la vidéo {props.id}.<br />
                  <br />
                </p>
                <div className="suppression-button-container">
                  <p>Êtes-vous sûr ?</p>
                  <Button variant="primary" onClick={handleClickSuppression}>
                    Accepter
                  </Button>{" "}
                </div>
              </Alert>
            </>
          )}
        </>
      );

}

export default DeleteVideo;