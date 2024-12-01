import AppContext from "components/AppContext/AppContext";
import Spinner from "components/shared/spinner/Spinner";
import { Format } from "models/format";
import { Groupe } from "models/groupe";
import { Video } from "models/video";
import React, { useEffect, useState } from "react";
import { Badge } from "react-bootstrap";
import { useNavigate } from "react-router-dom";

import * as videoService from "services/VideoService";
import CreateGroup from "./RestGroup/CreateGroup";

import "./ManageVideo.scss";
import UploadVideo from "./RestVideo/UploadVideo";
import DeleteGroup from "./RestGroup/DeleteGroup";
import ModifyGroup from "./RestGroup/ModifyGroup";
import ModifyVideo from "./RestVideo/ModifyVideo";
import DeleteVideo from "./RestVideo/DeleteVideo";

enum ManageVideoEnum {
  LIST_GROUP,
  LIST_VIDEO,
  MODIFY_GROUP,
  MODIFY_VIDEO,
  DELETE_GROUP,
  DELETE_VIDEO,
  ADD_GROUP,
  ADD_VIDEO,
}

function ManageVideo() {
  const context = React.useContext(AppContext);
  const navigate = useNavigate();

  const [groupes, setGroupes] = useState([new Groupe()]);
  const [videos, setVideos] = useState([new Video()]);
  const [manage, setManage] = useState(ManageVideoEnum.LIST_GROUP);
  const [activeGroup, setActiveGroup] = useState(new Groupe());
  const [activeVideo, setActiveVideo] = useState(new Video());

  const getAllVideos = async (id: number) => {
    const fetchedGetAllVideos = await videoService.getAllVideos(id);
    if (fetchedGetAllVideos.data && fetchedGetAllVideos.status === 200) {
      setVideos(fetchedGetAllVideos.data);
    }
  };

  const getAllGroupes = async () => {
    const fetchedGetAllGroupes = await videoService.getAllGroupes();
    if (fetchedGetAllGroupes.data && fetchedGetAllGroupes.status === 200) {
      console.log("ALL Groupes GET", fetchedGetAllGroupes);
      setGroupes(fetchedGetAllGroupes.data);
    }
  };

  useEffect(() => {
    getAllGroupes();
  }, []);

  function handleRetourGroupList() {
    setGroupes([new Groupe()]);
    getAllGroupes();
    setManage(ManageVideoEnum.LIST_GROUP);
  }

  function handleRetourVideoList(id: number) {
    setVideos([new Video()]);
    getAllVideos(id);
    setManage(ManageVideoEnum.LIST_VIDEO);
  }

  function handleAddGroupClick(): void {
    setActiveGroup(new Groupe());
    setManage(ManageVideoEnum.ADD_GROUP);
  }

  function handleAddVideoClick(): void {
    setActiveVideo(new Video());
    setManage(ManageVideoEnum.ADD_VIDEO);
  }

  function handleAccessGroupClick(id: number): void {
    let _groupe = groupes.find((element) => element.id === id);
    if (_groupe) {
      setActiveGroup(_groupe);
      getAllVideos(id);
      console.log(_groupe);
      setManage(ManageVideoEnum.LIST_VIDEO);
    }
  }

  function handleModifyGroupClick(id: number): void {
    let _groupe = groupes.find((element) => element.id === id);
    if (_groupe) {
      setActiveGroup(_groupe);
      getAllVideos(id);
      console.log(_groupe);
      setManage(ManageVideoEnum.MODIFY_GROUP);
    }
  }

  function handleDeleteGroupClick(id: number) {
    let _groupe = groupes.find((element) => element.id === id);
    if (_groupe) {
      setActiveGroup(_groupe);
      console.log(_groupe);
      setManage(ManageVideoEnum.DELETE_GROUP);
    }
  }

  function handleModifyVideoClick(id: number): void {
    let _video = videos.find((element) => element.id === id);
    if (_video) {
      setActiveVideo(_video);
      console.log(_video);
      setManage(ManageVideoEnum.MODIFY_VIDEO);
    }
  }

  function handleDeleteVideoClick(id: number) {
    let _video = videos.find((element) => element.id === id);
    if (_video) {
      setActiveVideo(_video);
      console.log(_video);
      setManage(ManageVideoEnum.DELETE_VIDEO);
    }
  }

  function redirectTo(path: string) {
    let first = path.lastIndexOf("/static/videos/");
    navigate("/watch/" + path.substring(first + 15));
  }

  return (
    <>
      <div className="manage-video-container">
        <>
          {manage === ManageVideoEnum.LIST_GROUP &&
            groupes.length > 0 &&
            groupes[0].id !== -1 &&
            groupes.map(
              (value: Groupe, index: React.Key | null | undefined) => {
                if (value.id !== -1) {
                  return (
                    <div key={index} className="user-list-entry grp-style">
                      <h4>{value.name}</h4>
                      <input
                        className="btn btn-primary"
                        type="submit"
                        onClick={() => handleAccessGroupClick(value.id)}
                        value="Consulter"
                      />
                      <input
                        className="btn btn-success m-1"
                        type="submit"
                        onClick={() => handleModifyGroupClick(value.id)}
                        value="Modifier"
                      />
                      <input
                        className="btn btn-danger m-1"
                        type="submit"
                        onClick={() => handleDeleteGroupClick(value.id)}
                        value="Supprimer"
                      />
                    </div>
                  );
                }
              }
            )}
          {manage === ManageVideoEnum.LIST_GROUP && (
            <div className="custom-button-creer-groupe">
              <input
                className="btn btn-primary "
                type="submit"
                onClick={handleAddGroupClick}
                value="Créer un groupe"
              />
            </div>
          )}
          {manage === ManageVideoEnum.MODIFY_GROUP && (
            <>
              <ModifyGroup id={activeGroup.id} />
              <div onClick={handleRetourGroupList} className="full-retour">
                Retour
              </div>
            </>
          )}
          {manage === ManageVideoEnum.DELETE_GROUP && (
            <>
              <DeleteGroup id={activeGroup.id} />
              <div onClick={handleRetourGroupList} className="full-retour">
                Retour
              </div>
            </>
          )}
          {manage === ManageVideoEnum.LIST_VIDEO &&
            videos.length > 0 &&
            videos[0].id !== -1 &&
            videos.map((value: Video, index: React.Key | null | undefined) => (
              <div key={index} className="user-list-entry video-style">
                <div className="">
                  <h4>{value.name}</h4>
                  <input
                    className="btn btn-primary h-25"
                    type="submit"
                    onClick={() => handleModifyVideoClick(value.id)}
                    value="Modifier"
                  />
                  {value.formats.map(
                    (val: Format, inde: React.Key | null | undefined) => (
                      <div key={inde}>
                        {val.quality === "Q240" && (
                          <Badge
                            bg="light"
                            text="dark"
                            onClick={() => redirectTo(val.path)}
                          >
                            240p
                          </Badge>
                        )}
                        {val.quality === "Q360" && (
                          <Badge
                            bg="light"
                            text="dark"
                            onClick={() => redirectTo(val.path)}
                          >
                            360p
                          </Badge>
                        )}
                        {val.quality === "Q480" && (
                          <Badge
                            bg="light"
                            text="dark"
                            onClick={() => redirectTo(val.path)}
                          >
                            480p
                          </Badge>
                        )}
                        {val.quality === "Q720" && (
                          <Badge
                            bg="light"
                            text="dark"
                            onClick={() => redirectTo(val.path)}
                          >
                            720p
                          </Badge>
                        )}
                        {val.quality === "Q1080" && (
                          <Badge
                            bg="light"
                            text="dark"
                            onClick={() => redirectTo(val.path)}
                          >
                            1080p
                          </Badge>
                        )}
                      </div>
                    )
                  )}
                  <input
                    className="btn btn-danger h-25"
                    type="submit"
                    onClick={() => handleDeleteVideoClick(value.id)}
                    value="Supprimer"
                  />
                </div>
              </div>
            ))}
          {manage === ManageVideoEnum.LIST_VIDEO && (
            <>
              <div className="custom-button-creer-groupe">
                <input
                  className="btn btn-primary"
                  type="submit"
                  onClick={handleAddVideoClick}
                  value="Upload une vidéo"
                />
                <div onClick={handleRetourGroupList} className="full-retour">
                  Retour
                </div>
              </div>
            </>
          )}
          {manage === ManageVideoEnum.MODIFY_VIDEO && activeVideo.id !== -1 && (
            <>
              <ModifyVideo id={activeVideo.id} />
              <div
                className="retour-style full-retour"
                onClick={() => handleRetourVideoList(activeGroup.id)}
              >
                Retour
              </div>
            </>
          )}
          {manage === ManageVideoEnum.MODIFY_VIDEO && activeVideo.id === -1 && (
            <Spinner />
          )}
          {manage === ManageVideoEnum.DELETE_VIDEO && activeVideo.id !== -1 && (
            <>
              <DeleteVideo id={activeVideo.id} />
              <div
                className="retour-style full-retour"
                onClick={() => handleRetourVideoList(activeGroup.id)}
              >
                Retour
              </div>
            </>
          )}
          {manage === ManageVideoEnum.DELETE_VIDEO && activeVideo.id === -1 && (
            <Spinner />
          )}
          {manage === ManageVideoEnum.ADD_GROUP && (
            <>
              <CreateGroup />
              <div
                className="retour-style full-retour"
                onClick={handleRetourGroupList}
              >
                Retour
              </div>
            </>
          )}
          {manage === ManageVideoEnum.ADD_VIDEO && (
            <>
              <UploadVideo idGroup={activeGroup.id} />
              <div
                className="retour-style full-retour"
                onClick={() => handleRetourVideoList(activeGroup.id)}
              >
                Retour
              </div>
            </>
          )}
        </>
      </div>
    </>
  );
}

export default ManageVideo;
