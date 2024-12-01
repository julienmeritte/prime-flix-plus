import { useState } from "react";
import { Button, Form } from "react-bootstrap";

import { Groupe } from "models/groupe";

import * as videoService from "services/VideoService";

function CreateGroup() {
  const [ready, setReady] = useState(false);
  const [group, setGroup] = useState(new Groupe());
  const [isSerie, setIsSerie] = useState(false);

  const createGroupForm = async () => {
    const fetchedCreateGroupForm = await videoService.createGroup(
      group.name,
      isSerie
    );
    if (fetchedCreateGroupForm.data && fetchedCreateGroupForm.status === 200) {
      console.log(fetchedCreateGroupForm.data);
      setReady(true);
    }
  };

  function handleChangeName(e: any) {
    let _group = group;
    _group.name = e.target.value;
    setGroup({ ..._group });
  }

  function handleSubmit(event: any) {
    event.preventDefault();
    console.log("GROUP A SUBMIT", group);
    createGroupForm();
  }

  function handleChangeSerie() {
    setIsSerie(!isSerie);
  }

  return (
    <div className="upload-video-container">
      {ready ? (
        <div>Création avec succès</div>
      ) : (
        <>
          <Form className="upload-form form-style" onSubmit={handleSubmit}>
            <h3>Création d'un nouveau groupe de vidéos</h3>
            <Form.Group className="mb-3" controlId="name">
              <Form.Label>Nom du groupe</Form.Label>
              <Form.Control
                required
                type="text"
                placeholder="Renseignez le nom"
                onChange={handleChangeName}
              />
            </Form.Group>
            <Form.Check.Input
              type="checkbox"
              isValid
              onChange={handleChangeSerie}
            />
            <Form.Check.Label>Est une série</Form.Check.Label>
            <Button variant="primary" type="submit">
              Envoyer
            </Button>
          </Form>
        </>
      )}
    </div>
  );
}

export default CreateGroup;
