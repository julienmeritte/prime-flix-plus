import { useEffect, useState } from "react";
import { Alert, Button } from "react-bootstrap";

import "./RestGroup.scss";

import * as videoService from "services/VideoService";

interface DeleteGroupProps {
  id: number;
}

function DeleteGroup(props: DeleteGroupProps) {
  const [ready, setReady] = useState(false);

  const deleteGroupForm = async () => {
    const fetchedDeleteGroupForm = await videoService.deleteGroup(props.id);
    if (fetchedDeleteGroupForm.data && fetchedDeleteGroupForm.status === 201) {
      console.log(fetchedDeleteGroupForm.data);
      setReady(true);
    }
  };

  function handleClickSuppression() {
    deleteGroupForm();
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
              Vous êtes sur le point de détruire le groupe {props.id}.<br />
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

export default DeleteGroup;
