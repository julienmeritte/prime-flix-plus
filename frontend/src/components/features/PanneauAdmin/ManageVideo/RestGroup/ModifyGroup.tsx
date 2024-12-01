import { useEffect, useState } from "react";
import { Button, Form } from "react-bootstrap";

import { Groupe } from "models/groupe";

import * as videoService from "services/VideoService";

import "./RestGroup.scss";

interface ModifyGroupProps {
  id: number;
}

function Modify(props: ModifyGroupProps) {
  const [ready, setReady] = useState(false);
  const [groupe, setGroupe] = useState(new Groupe());

  const modifyGroupForm = async () => {
    const fetchedModifyGroupForm = await videoService.modifyGroupe(
      props.id,
      groupe.name
    );
    if (fetchedModifyGroupForm.data && fetchedModifyGroupForm.status === 201) {
      console.log(fetchedModifyGroupForm.data);
      setReady(true);
    }
  };

  const getGroupe = async () => {
    const fetchedGetGroupe = await videoService.getGroupe(props.id);
    if (fetchedGetGroupe.data && fetchedGetGroupe.status === 200) {
      console.log("On Groupe GET", fetchedGetGroupe);
      setGroupe(fetchedGetGroupe.data);
    }
  };

  useEffect(() => {
    getGroupe();
  }, []);

  function handleSubmit(event: any) {
    event.preventDefault();
    modifyGroupForm();
  }

  function handleChangeName(e: any) {
    let _groupe = groupe;
    _groupe.name = e.target.value;
    setGroupe({ ..._groupe });
  }

  function handleChangeSerie() {
    let _group = groupe;
    _group.isSerie = !_group.isSerie;
    setGroupe({ ..._group });
  }

  return (
    <>
      {ready ? (
        <div>Modifiéavec succès</div>
      ) : (
        <>
          <div className="modification-groupe-container">
            <Form className="upload-form form-style" onSubmit={handleSubmit}>
              <h3>Modification du groupe</h3>
              <Form.Group className="mb-3" controlId="name">
                <Form.Label>Nom du groupe</Form.Label>
                <Form.Control
                  required
                  type="text"
                  placeholder="Renseignez le nom"
                  value={groupe.name}
                  onChange={handleChangeName}
                />
                <Form.Check.Input
                  type="checkbox"
                  isValid
                  onChange={handleChangeSerie}
                  checked={groupe.isSerie}
                />
              </Form.Group>
              <Button variant="primary" type="submit">
                Envoyer
              </Button>
            </Form>
          </div>
        </>
      )}
    </>
  );
}

export default Modify;
