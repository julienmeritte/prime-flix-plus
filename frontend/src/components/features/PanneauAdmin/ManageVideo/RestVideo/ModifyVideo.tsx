import { useEffect, useState } from "react";

import { Video } from "models/video";
import { Button, Form } from "react-bootstrap";

import * as videoService from "services/VideoService";

interface ModifyVideoProps {
  id: number;
}

function ModifyVideo(props: ModifyVideoProps) {
  const [ready, setReady] = useState(false);
  const [video, setVideo] = useState(new Video());

  const getVideo = async () => {
    const fetchedGetVideo = await videoService.getVideo(props.id);
    if (fetchedGetVideo.data && fetchedGetVideo.status === 200) {
      console.log("On Video GET", fetchedGetVideo);
      setVideo(fetchedGetVideo.data);
    }
  };

  const updateVideo = async () => {
    const fetchedUpdateVideo = await videoService.updateVideo(props.id, video);
    if (fetchedUpdateVideo.data && fetchedUpdateVideo.status === 201) {
      setVideo(fetchedUpdateVideo.data);
      setReady(true);
    }
  };

  useEffect(() => {
    getVideo();
  }, []);

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

  function handleSubmit(event: any) {
    event.preventDefault();
    console.log("VIDEO A SUBMIT", video);
    updateVideo();
  }

  useEffect(() => {
    console.log(video);
  }, [video]);

  return (
    <>
      {ready ? (
        <div>Modifié avec succès</div>
      ) : (
        <>
          <div className="modification-groupe-container">
            
            <Form className="upload-form form-style-video-modifier" onSubmit={handleSubmit}>
            <h3>Modification de la vidéo</h3>
              <Form.Group className="mb-3" controlId="name">
                <Form.Label>Nom de la vidéo</Form.Label>
                <Form.Control
                  required
                  type="text"
                  placeholder="Renseignez le nom"
                  defaultValue={video.name}
                  onChange={handleChangeName}
                />
              </Form.Group>
              <Form.Group className="mb-3" controlId="description">
                <Form.Label>Description de la vidéo</Form.Label>
                <Form.Control
                  required
                  as="textarea"
                  rows={3}
                  defaultValue={video.description}
                  onChange={handleChangeDescription}
                />
              </Form.Group>
              <Form.Group className="mb-3" controlId="creator">
                <Form.Label>Créateur</Form.Label>
                <Form.Control
                  required
                  type="text"
                  placeholder="Renseignez le créateur"
                  defaultValue={video.creator}
                  onChange={handleChangeCreator}
                />
              </Form.Group>
              <Form.Group className="mb-3" controlId="distribution">
                <Form.Label>Distributeur</Form.Label>
                <Form.Control
                  required
                  type="text"
                  placeholder="Renseignez la distribution"
                  defaultValue={video.distribution}
                  onChange={handleChangeDistribution}
                />
              </Form.Group>
              <Form.Group className="mb-3" controlId="age">
                <Form.Label>Age conseillé</Form.Label>
                <Form.Select
                  aria-label="Sélectionnez un age"
                  onChange={handleChangeAge}
                  value={video.age}
                >
                  <option value="YOUNG">Jeunesse</option>
                  <option value="ALL_PUBLIC">Tout public</option>
                  <option value="ADVISED_PUBLIC">Public averti</option>
                  <option value="MINUS_12" selected>
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
                  defaultValue={video.date}
                  onChange={handleChangeDate}
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

export default ModifyVideo;
