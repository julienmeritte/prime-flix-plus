import AppContext from "components/AppContext/AppContext";
import { Profile } from "models/profile";
import { User } from "models/user";
import React from "react";
import { ImageProfile } from "enums/ImageProfile";
import { useEffect, useState } from "react";
import * as profileService from "services/ProfileService";
import { useNavigate } from "react-router-dom";
import { Alert, Button, Form } from "react-bootstrap";

import "./Utilisateur.scss";

enum ManageUserEnum {
  SHOW,
  MODIFY,
  DELETE,
}

function Utilisateur() {
  const context = React.useContext(AppContext);
  const [localProfile, setLocalProfile] = useState(new Profile());
  const [manage, setManage] = useState(ManageUserEnum.SHOW);
  const [images, setImages] = useState([""]);
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();
  const [ready, setReady] = useState(false);

  const modifyProfile = async () => {
    setLoading(true);
    const fetchedModifyProfile = await profileService.modifyProfile(
      localProfile
    );
    if (fetchedModifyProfile) {
      setLocalProfile(fetchedModifyProfile);
      context.setActiveProfile(fetchedModifyProfile);
      console.log("FETCHED RESPONSE", fetchedModifyProfile);
      setReady(true);
    }
  };

  const deleteProfileForm = async () => {
    const fetchedDeleteProfileForm = await profileService.deleteProfile(
      localProfile.id
    );
    if (
      fetchedDeleteProfileForm.data &&
      fetchedDeleteProfileForm.status === 200
    ) {
      console.log(fetchedDeleteProfileForm.data);
      setReady(true);
      context.setActiveProfile(new Profile());
      window.location.href = "/";
    }
  };

  useEffect(() => {
    if (!context.user.enabled || context.activeProfile.id < 0) {
      navigate("/");
    }
    let _profile = context.activeProfile;
    setLocalProfile(_profile);
    let _images: string[] = [];
    for (let item in ImageProfile) {
      _images.push(ImageProfile[item as keyof typeof ImageProfile]);
    }
    setImages(_images);
  }, []);

  function handleModifyClick() {
    setManage(ManageUserEnum.MODIFY);
  }

  function handleDeleteClick() {
    setManage(ManageUserEnum.DELETE);
  }

  function handleRetourList() {
    setReady(false);
    setManage(ManageUserEnum.SHOW);
  }

  function handleRetourNavigate() {
    setReady(false);
    setManage(ManageUserEnum.SHOW);
  }

  function handleModifyFormSeries() {
    let _profile = localProfile;
    _profile.receiveNewSeries = !localProfile.receiveNewSeries;
    setLocalProfile({ ..._profile });
  }

  function handleModifyFormFilms() {
    let _profile = localProfile;
    _profile.receiveNewFilms = !localProfile.receiveNewFilms;
    setLocalProfile({ ..._profile });
  }

  function handleModifyFormNews() {
    let _profile = localProfile;
    _profile.receiveNewsletter = !localProfile.receiveNewsletter;
    setLocalProfile({ ..._profile });
  }

  function handleModifyFormSeasons() {
    let _profile = localProfile;
    _profile.receiveNewSeasons = !localProfile.receiveNewSeasons;
    setLocalProfile({ ..._profile });
  }

  function handleModifyFormPseudo(event: any) {
    let _profile = localProfile;
    _profile.pseudo = event.target.value;
    setLocalProfile({ ..._profile });
  }

  function getImageFromEnum(imageProfile: string): string {
    let link = "images/profiles/" + imageProfile + ".png";
    return link;
  }

  function setImageForm(value: string) {
    let _profile = localProfile;
    _profile.image = value;
    setLocalProfile({ ..._profile });
  }

  useEffect(() => {
    console.log("localprofile", localProfile);
  }, [localProfile]);

  function handleSubmitModify(event: any) {
    event.preventDefault();
    modifyProfile();
  }

  function handleClickSuppression() {
    deleteProfileForm();
  }

  // TODO Page profil utilisateur
  return (
    <>
      <div className="manage-user-container">
        {ready ? (
          <>
            <div className="profil-modif">Profil modifié avec succès</div>
            <p onClick={handleRetourList} className="full-retour">
              Retour
            </p>
          </>
        ) : (
          <>
            {manage === ManageUserEnum.SHOW && (
              <>
                <h2>Profil utilisateur</h2>
                <div className="user-profile">
                  <div>
                    <img src={getImageFromEnum(localProfile.image)} />
                    <p>{localProfile.pseudo}</p>
                  </div>
                  <Button onClick={handleModifyClick}>Modifier</Button>
                  <Button onClick={handleDeleteClick}>Supprimer</Button>
                </div>
              </>
            )}
            {manage === ManageUserEnum.MODIFY && localProfile.id >= 0 && (
              <>
                <h2 className="profil-modif">Modification de votre profil</h2>
                <br />
                <Form className="upload-form" onSubmit={handleSubmitModify}>
                  <Form.Group className="mb-3" controlId="name">
                    <Form.Label>Pseudo</Form.Label>
                    <Form.Control
                      required
                      type="text"
                      placeholder="Renseignez le pseudo"
                      defaultValue={localProfile.pseudo}
                      onChange={handleModifyFormPseudo}
                    />
                  </Form.Group>
                  <br />
                  <div className="selection-image">
                    {images.map((value, index) => {
                      return (
                        <div className="profile-image" key={index}>
                          <img
                            className="img-style"
                            src={getImageFromEnum(value)}
                            onClick={() => setImageForm(value)}
                          />
                          {localProfile.image === value ? (
                            <img
                              className="profile-image-superposition"
                              src="images/verifier.png"
                            />
                          ) : (
                            <></>
                          )}
                        </div>
                      );
                    })}
                  </div>
                  <br />
                  <Form.Check
                    type="switch"
                    id="custom-switch"
                    label="Recevoir les notifications des nouveaux films"
                    onClick={handleModifyFormFilms}
                    defaultChecked={localProfile.receiveNewFilms}
                  />
                  <Form.Check
                    type="switch"
                    id="custom-switch"
                    label="Recevoir les notifications des nouvelles saisons"
                    onClick={handleModifyFormSeasons}
                    defaultChecked={localProfile.receiveNewSeasons}
                  />
                  <Form.Check
                    type="switch"
                    id="custom-switch"
                    label="Recevoir les notifications des nouvelles séries"
                    onClick={handleModifyFormSeries}
                    defaultChecked={localProfile.receiveNewSeries}
                  />
                  <Form.Check
                    type="switch"
                    id="custom-switch"
                    label="Recevoir la newsletter"
                    onClick={handleModifyFormNews}
                    defaultChecked={localProfile.receiveNewsletter}
                  />
                  <br />
                  <Button variant="primary" type="submit">
                    Envoyer
                  </Button>
                </Form>
                <p onClick={handleRetourList} className="full-retour">
                  Retour
                </p>
              </>
            )}
            {manage === ManageUserEnum.DELETE && localProfile.id >= 0 && (
              <>
                <h2>Suppression du profil</h2>
                <>
                  <Alert variant="danger" className="surcharge-alert-danger">
                    <Alert.Heading>Attention</Alert.Heading>
                    <p>
                      Vous êtes sur le point de supprimer votre profil.
                      <br />
                      <br />
                    </p>
                    <div className="suppression-button-container">
                      <p>Êtes-vous sûr ?</p>
                      <Button
                        variant="primary"
                        onClick={handleClickSuppression}
                      >
                        Accepter
                      </Button>{" "}
                    </div>
                  </Alert>
                </>
                <p onClick={handleRetourNavigate} className="full-retour">
                  Retour
                </p>
              </>
            )}
          </>
        )}
      </div>
    </>
  );
}

export default Utilisateur;
