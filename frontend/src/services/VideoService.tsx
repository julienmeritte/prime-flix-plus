import axios from "axios";
import { Video } from "models/video";

export async function getAllGroupes() {
  return axios
    .get(`/api/video/all/group`, {
      headers: {
        "Content-Type": "application/json",
      },
    })
    .then((res) => {
      return res;
    })
    .catch((err) => {
      return err;
    });
}

export async function getAllVideos(id: number) {
  return axios
    .get(`/api/video/all/videos/` + id, {
      headers: {
        "Content-Type": "application/json",
      },
    })
    .then((res) => {
      return res;
    })
    .catch((err) => {
      return err;
    });
}

export async function uploadVideo(
  file: any,
  fileImage: any,
  id: number,
  video: Video
) {
  const formData = new FormData();
  formData.append("source", file);
  formData.append("image", fileImage);
  formData.append(
    "video",
    JSON.stringify({
      name: video.name,
      description: video.description,
      creator: video.creator,
      distribution: video.distribution,
      age: video.age,
      groupId: id,
      date: video.date,
      categories: video.categorie,
    })
  );

  return axios
    .post(`/api/video/add`, formData, {
      headers: {
        "Content-Type": "application/json",
      },
    })
    .then((res) => {
      return res;
    })
    .catch((err) => {
      return err;
    });
}

export async function createGroup(name: string, isSerie: boolean) {
  return axios
    .post(
      `/api/video/add/group`,
      JSON.stringify({
        name: name,
        isSerie: isSerie,
      }),
      {
        headers: {
          "Content-Type": "application/json",
        },
      }
    )
    .then((res) => {
      return res;
    })
    .catch((err) => {
      return err;
    });
}

export async function deleteGroup(id: number) {
  return axios
    .delete(`/api/video/delete/group/` + id, {
      headers: {
        "Content-Type": "application/json",
      },
    })
    .then((res) => {
      return res;
    })
    .catch((err) => {
      return err;
    });
}

export async function deleteVideo(id: number) {
  return axios
    .delete(`/api/video/delete/video/` + id, {
      headers: {
        "Content-Type": "application/json",
      },
    })
    .then((res) => {
      return res;
    })
    .catch((err) => {
      return err;
    });
}

export async function getGroupe(id: number) {
  return axios
    .get(`/api/video/get/group/` + id, {
      headers: {
        "Content-Type": "application/json",
      },
    })
    .then((res) => {
      return res;
    })
    .catch((err) => {
      return err;
    });
}

export async function modifyGroupe(id: number, name: string) {
  return axios
    .put(
      `/api/video/modify/group/` + id,
      {
        name: name,
      },
      {
        headers: {
          "Content-Type": "application/json",
        },
      }
    )
    .then((res) => {
      return res;
    })
    .catch((err) => {
      return err;
    });
}

export async function getVideo(id: number) {
  return axios
    .get(`/api/video/get/video/` + id, {
      headers: {
        "Content-Type": "application/json",
      },
    })
    .then((res) => {
      return res;
    })
    .catch((err) => {
      return err;
    });
}

export async function updateVideo(id: number, video: Video) {
  return axios
    .put(
      `/api/video/modify/video/` + id,
      {
        name: video.name,
        description: video.description,
        creator: video.creator,
        distribution: video.distribution,
        age: video.age,
        date: video.date,
      },
      {
        headers: {
          "Content-Type": "application/json",
        },
      }
    )
    .then((res) => {
      return res;
    })
    .catch((err) => {
      return err;
    });
}

export async function getImage(id: number) {
  return axios
    .get(`/api/stream/image/` + id, { responseType: "blob" })
    .then((res) => {
      return URL.createObjectURL(res.data);
    })
    .catch((err) => {
      return err;
    });
}

export async function getSortAll() {
  return axios
    .get(`/api/video/sort/recent`, {
      headers: {
        "Content-Type": "application/json",
      },
    })
    .then((res) => {
      return res.data;
    })
    .catch((err) => {
      return err;
    });
}
