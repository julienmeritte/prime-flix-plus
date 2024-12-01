import { Video } from "models/video";
import { useEffect, useState } from "react";
import { Button, Form } from "react-bootstrap";

import "./UploadVideo.scss";

import * as videoService from "services/VideoService";

import { VideoCategory } from "enums/VideoCategory";
import { useNavigate } from "react-router-dom";

interface UploadProps {
  idGroup: number;
}

function UploadVideo(props: UploadProps) {
  const [video, setVideo] = useState(new Video());
  const [file, setFile] = useState(null);
  const [fileImage, setFileImage] = useState(null);
  const [ready, setReady] = useState(false);

  const navigate = useNavigate();

  const sendVideo = async () => {
    const fetchedsendVideo = await videoService.uploadVideo(
      file,
      fileImage,
      props.idGroup,
      video
    );
    if (fetchedsendVideo.data && fetchedsendVideo.status === 201) {
      setReady(true);
    }
  };

  function handleChangeName(e: any) {
    let _video = video;
    _video.name = e.target.value;
    setVideo({ ..._video });
  }

  function handleChangeDescription(e: any) {
    let _video = video;
    _video.description = e.target.value;
    setVideo({ ..._video });
  }

  function handleChangeCreator(e: any) {
    let _video = video;
    _video.creator = e.target.value;
    setVideo({ ..._video });
  }

  function handleChangeDistribution(e: any) {
    let _video = video;
    _video.distribution = e.target.value;
    setVideo({ ..._video });
  }

  function handleChangeAge(e: any) {
    let _video = video;
    _video.age = e.target.value;
    setVideo({ ..._video });
  }

  function handleChangeDate(e: any) {
    let _video = video;
    _video.date = e.target.value;
    setVideo({ ..._video });
  }

  function handleChangeFile(e: any) {
    setFile(e.target.files[0]);
  }

  function handleChangeFileImage(e: any) {
    setFileImage(e.target.files[0]);
  }

  function handleChangeCategory(type: string) {
    let _video = video;

    if (_video.categorie.some((e) => e === type)) {
      let _index = _video.categorie.findIndex((d) => d === type);
      _video.categorie.splice(_index);
    } else _video.categorie.push(type);
    setVideo({ ..._video });
  }

  function handleSubmit(event: any) {
    event.preventDefault();
    console.log("VIDEO A SUBMIT", video, file);
    sendVideo();
  }

  useEffect(() => {
    console.log(video);
  }, [video]);

  return (
    <div className="upload-video-container">
      {ready ? (
        <div>
          Upload envoyé avec succès. Les différents formats seront bientôt
          disponibles.
        </div>
      ) : (
        <>
          <Form
            className="upload-form form-style-video-upload"
            onSubmit={handleSubmit}
          >
            <h3>Formulaire d'upload vidéo</h3>
            <Form.Group className="mb-3" controlId="name">
              <Form.Label>Nom de la vidéo</Form.Label>
              <Form.Control
                required
                type="text"
                placeholder="Renseignez le nom"
                onChange={handleChangeName}
              />
            </Form.Group>
            <Form.Group className="mb-3" controlId="description">
              <Form.Label>Description de la vidéo</Form.Label>
              <Form.Control
                required
                as="textarea"
                rows={3}
                onChange={handleChangeDescription}
              />
            </Form.Group>
            <Form.Group className="mb-3" controlId="creator">
              <Form.Label>Créateur</Form.Label>
              <Form.Control
                required
                type="text"
                placeholder="Renseignez le créateur"
                onChange={handleChangeCreator}
              />
            </Form.Group>
            <Form.Group className="mb-3" controlId="distribution">
              <Form.Label>Distributeur</Form.Label>
              <Form.Control
                required
                type="text"
                placeholder="Renseignez la distribution"
                onChange={handleChangeDistribution}
              />
            </Form.Group>
            <Form.Group className="mb-3" controlId="age">
              <Form.Label>Age conseillé</Form.Label>
              <Form.Select
                aria-label="Sélectionnez un age"
                onChange={handleChangeAge}
              >
                <option value="YOUNG">Jeunesse</option>
                <option value="ALL_PUBLIC">Tout public</option>
                <option value="ADVISED_PUBLIC">Public averti</option>
                <option value="MINUS_12">
                  Déconseillé aux moins de 12 ans
                </option>
                <option value="MINUS_16">
                  Déconseillé aux moins de 16 ans
                </option>
                <option value="MINUS_18">
                  Déconseillé aux moins de 18 ans
                </option>
              </Form.Select>
            </Form.Group>
            <Form.Group className="mb-3" controlId="date">
              <Form.Label>Date de sortie</Form.Label>
              <Form.Control
                required
                type="date"
                placeholder="Renseignez la date de sortie"
                onChange={handleChangeDate}
              />
            </Form.Group>
            <Form.Group controlId="formFile" className="mb-3">
              <Form.Label>Vidéo de base à uploader</Form.Label>
              <Form.Control required type="file" onChange={handleChangeFile} />
            </Form.Group>
            <Form.Group controlId="formFile" className="mb-3">
              <Form.Label>Miniature de la vidéo</Form.Label>
              <Form.Control
                required
                type="file"
                onChange={handleChangeFileImage}
              />
            </Form.Group>
            <Form.Check type="checkbox" required>
              {(
                Object.keys(VideoCategory) as Array<keyof typeof VideoCategory>
              ).map((type) => (
                <>
                  <Form.Check.Input
                    type="checkbox"
                    isValid
                    onChange={() => handleChangeCategory(type)}
                  />
                  <Form.Check.Label>{type}</Form.Check.Label>
                  <br />
                </>
              ))}
            </Form.Check>

            <Button variant="primary" type="submit">
              Envoyer
            </Button>
          </Form>
        </>
      )}
    </div>
  );
}

export default UploadVideo;
