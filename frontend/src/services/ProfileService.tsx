import axios from "axios";
import { Profile } from "models/profile";

export async function createMainProfile(name: string, image: string) {
  return axios
    .post(
      `/api/profile/addMain`,
      JSON.stringify({
        pseudo: name,
        image: image,
      }),
      {
        headers: {
          "Content-Type": "application/json",
        },
      }
    )
    .then((res) => {
      return res.data;
    })
    .catch((err) => {
      return err;
    });
}

export async function createProfile(
  name: string,
  image: string,
  isYoung: boolean
) {
  return axios
    .post(
      `/api/profile/add`,
      JSON.stringify({
        pseudo: name,
        image: image,
        isYoung: isYoung,
      }),
      {
        headers: {
          "Content-Type": "application/json",
        },
      }
    )
    .then((res) => {
      return res.data;
    })
    .catch((err) => {
      return err;
    });
}

export async function modifyProfile(profile: Profile) {
  return axios
    .put(
      `/api/profile`,
      JSON.stringify({
        id: profile.id,
        pseudo: profile.pseudo,
        image: profile.image,
        receiveNewsletter: profile.receiveNewsletter,
        receiveNewSeries: profile.receiveNewSeries,
        receiveNewFilms: profile.receiveNewFilms,
        receiveNewSeasons: profile.receiveNewSeasons,
      }),
      {
        headers: {
          "Content-Type": "application/json",
        },
      }
    )
    .then((res) => {
      return res.data;
    })
    .catch((err) => {
      return err;
    });
}

export async function deleteProfile(id: number) {
  return axios
    .delete(`/api/profile/` + id, {
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
