import { User } from "models/user";
import axios from "axios";
import { UserRole } from "enums/UserRole";
import internal from "stream";
import { idText } from "typescript";

export function authenticateUser(mail: string, password: string) {
  return axios
    .post(
      "/api/auth/authenticate",
      JSON.stringify({
        mail: mail,
        password: password,
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

export async function getMe() {
  return axios
    .get(`/api/user/me`)
    .then((res) => {
      let _user = new User();
      _user = res.data;
      _user.role = getEnumRoleFromResponse(res.data.role);
      return _user;
    })
    .catch((err) => {
      return new User();
    });
}

export async function register(mail: string, password: string) {
  return axios
    .post(
      `/api/auth/register`,
      JSON.stringify({
        mail: mail,
        password: password,
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

export async function validationMail(code: string) {
  return axios
    .post(
      `/api/auth/validation`,
      JSON.stringify({
        code: code,
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

export async function getAllUsers() {
  return axios
    .get(`/api/user/get/all`, {
      headers: {
        "Content-Type": "application/json",
      },
    })
    .then((res) => {
      let _users: User[] = [];
      _users = res.data;
      for (let i = 0; i < _users.length; i++) {
        _users[i].role = getEnumRoleFromResponse(res.data[i].role);
      }
      return res.data;
    })
    .catch((err) => {
      return err;
    });
}

export async function modifyUser(id: number, newUser: User) {
  return axios
    .put(
      `/api/user/` + id,
      JSON.stringify({
        mail: newUser.mail,
        enabled: newUser.enabled,
        role: newUser.role,
      }),
      {
        headers: {
          "Content-Type": "application/json",
        },
      }
    )
    .then((res) => {
      let _user = new User();
      _user = res.data;
      _user.role = getEnumRoleFromResponse(res.data.role);
      return _user;
    })
    .catch((err) => {
      return err;
    });
}

export async function deleteUser(id: number) {
  return axios
    .delete(`/api/user/` + id, {
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

export function getEnumRoleFromResponse(_role: string) {
  switch (_role) {
    case "ROLE_USER":
      return UserRole.ROLE_USER;
    case "ROLE_SUPPORT":
      return UserRole.ROLE_SUPPORT;
    case "ROLE_AFFILIATE":
      return UserRole.ROLE_AFFILIATE;
    case "ROLE_ADMIN":
      return UserRole.ROLE_ADMIN;
    default:
      return UserRole.ROLE_USER;
  }
}

function getDifferenceBetweenUsers(user: User, newUser: User): UserDto {
  let _user = new UserDto();
  if (user.mail !== newUser.mail) _user.mail = newUser.mail;
  if (user.enabled !== newUser.enabled) _user.enabled = newUser.enabled;
  if (user.role !== newUser.role) _user.role = newUser.role;
  console.log("_user", _user);
  return _user;
}

class UserDto {
  mail: string | null = null;
  password: string | null = null;
  enabled: boolean | null = null;
  role: UserRole | null = null;
}
