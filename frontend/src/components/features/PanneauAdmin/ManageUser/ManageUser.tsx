import AppContext from "components/AppContext/AppContext";
import { UserRole } from "enums/UserRole";
import { User } from "models/user";
import React from "react";
import { useEffect, useState } from "react";
import { Alert, Button, Pagination, Table } from "react-bootstrap";

import * as userService from "services/UserService";

import "./ManageUser.scss";

enum ManageUserEnum {
  LIST,
  MODIFY,
  DELETE,
}

function ManageUser() {
  const context = React.useContext(AppContext);
  const [listUsers, setListUsers] = useState([new User()]);
  const [loadingList, setLoadingList] = useState(true);
  const [selectedUser, setSelectedUser] = useState(new User());
  const [searchText, setSearchtext] = useState("");
  const [manage, setManage] = useState(ManageUserEnum.LIST);
  const [loading, setLoading] = useState(false);

  const [pagination, setPagination] = useState([]);
  const [page, setPage] = useState(1);

  useEffect(() => {
    const getAllUsers = async () => {
      const fetchedGetAllUsers = await userService.getAllUsers();
      if (fetchedGetAllUsers) {
        console.log("ALL USERS GET", fetchedGetAllUsers);
        setListUsers(fetchedGetAllUsers);
      }
    };
    getAllUsers();
  }, []);

  useEffect(() => {
    if (listUsers.length > 0) {
      /*for (let number = 1; number <= listUsers.length; number++) {
        let _pagination = pagination;
        _pagination.push(
          <Pagination.Item key={number} active={number === page}>
            {number}
          </Pagination.Item>
        );
        setPagination({ ..._pagination });
      }*/
    }
  }, [listUsers]);

  function handleChangeSearchText(event: any) {
    setSearchtext(event.target.value);
  }

  function handleRetourList() {
    setManage(ManageUserEnum.LIST);
  }

  function handleModifyClick(id: number) {
    let _user = listUsers.find((element) => element.id === id);
    if (_user) {
      setSelectedUser(_user);
      console.log(_user);
      setManage(ManageUserEnum.MODIFY);
    }
  }

  function handleDeleteClick(id: number) {
    let _user = listUsers.find((element) => element.id === id);
    if (_user) {
      setSelectedUser(_user);
      setManage(ManageUserEnum.DELETE);
    }
  }

  // onChangeForm Modify

  function handleModifyFormRole(event: any) {
    let _user = selectedUser;
    _user.role = Number(event.target.value);
    setSelectedUser({ ..._user });
  }

  function handleModifyFormMail(event: any) {
    let _user = selectedUser;
    _user.mail = event.target.value;
    setSelectedUser({ ..._user });
  }

  function handleModifyFormEnabled() {
    let _user = selectedUser;
    _user.enabled = !selectedUser.enabled;
    setSelectedUser({ ..._user });
  }

  function handleSubmitModify(event: any) {
    event.preventDefault();
    const modifyUser = async () => {
      setLoading(true);
      const fetchedModifyUser = await userService.modifyUser(
        selectedUser.id,
        selectedUser
      );
      if (fetchedModifyUser) {
        let _users = listUsers;
        let foundIndex = _users.findIndex((x) => x.id == fetchedModifyUser.id);
        _users[foundIndex] = fetchedModifyUser;
        setSelectedUser(fetchedModifyUser);
        setListUsers(listUsers);
        console.log("FETCHED RESPONSE", fetchedModifyUser);
        window.location.href = "/";
      }
    };

    modifyUser();
  }

  const deleteUserForm = async () => {
    const fetchedDeleteUserForm = await userService.deleteUser(selectedUser.id);
    if (fetchedDeleteUserForm.data && fetchedDeleteUserForm.status === 200) {
      console.log(fetchedDeleteUserForm.data);
      setManage(ManageUserEnum.LIST);
      window.location.href = "/";
    }
  };

  function handleClickSuppression() {
    deleteUserForm();
  }

  useEffect(() => {
    console.log("SELECTED USER CHANGE", selectedUser);
  }, [selectedUser]);

  return (
    <>
      <div className="manage-user-container form-style-user">
        {manage === ManageUserEnum.LIST && (
          <>
            <h2 className="head-style">Liste des utilisateurs</h2>
            <input
              className="input-style"
              type="text"
              name="searchText"
              value={searchText}
              onChange={handleChangeSearchText}
            />
            <br />
            <div className="div-table">
              <table className="table-style">
                <thead>
                  <tr className="tr-style">
                    <th className="th-style">Email</th>
                    <th className="th-style"></th>
                    <th className="th-style"></th>
                  </tr>
                </thead>
                <tbody>
                  {listUsers.map(
                    (value: User, index: React.Key | null | undefined) => {
                      if (
                        value.mail.includes(searchText) &&
                        value.id !== context.user.id
                      ) {
                        return (
                          <tr className="tr-style">
                            <td className="td-style" width="10%">
                              {value.mail}
                            </td>
                            <td
                              className="td-style"
                              width="10%"
                              onClick={() => handleModifyClick(value.id)}
                            >
                              Modifier
                            </td>
                            <td
                              className="td-style"
                              width="10%"
                              onClick={() => handleDeleteClick(value.id)}
                            >
                              Supprimer
                            </td>
                          </tr>
                        );
                      }
                    }
                  )}
                </tbody>
              </table>
            </div>
          </>
        )}
        {manage === ManageUserEnum.MODIFY && selectedUser.id >= 0 && (
          <>
            <form
              className="manage-user-container form-style-modify-user"
              onSubmit={handleSubmitModify}
            >
              <h2>Modification de l'utilisateur n°{selectedUser.id}</h2>
              <br />
              <input
                className="input-style-modify-user"
                type="text"
                name="mail"
                value={selectedUser.mail}
                onChange={handleModifyFormMail}
              />
              <br />
              <select
                name="role"
                value={selectedUser.role}
                onChange={handleModifyFormRole}
              >
                <option value={UserRole.ROLE_USER}>ROLE_USER</option>
                <option value={UserRole.ROLE_SUPPORT}>ROLE_SUPPORT</option>
                <option value={UserRole.ROLE_AFFILIATE}>ROLE_AFFILIATE</option>
                <option value={UserRole.ROLE_ADMIN}>ROLE_ADMIN</option>
              </select>
              <br />
              <label className="label-style">Compte activé ? </label>
              <input
                className="input-style-checkbox-modify-user"
                type="checkbox"
                defaultChecked={selectedUser.enabled}
                onClick={handleModifyFormEnabled}
              />
              <br />
              <input
                type="submit"
                className="btn btn-success"
                value="modifier"
              />
            </form>
            <p onClick={handleRetourList} className="full-retour">
              Retour
            </p>
          </>
        )}
        {manage === ManageUserEnum.DELETE && selectedUser.id >= 0 && (
          <>
            <h2>Suppression de l'utilisateur n°{selectedUser.id}</h2>
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
                  <Button variant="primary" onClick={handleClickSuppression}>
                    Accepter
                  </Button>{" "}
                </div>
              </Alert>
            </>
            <p onClick={handleRetourList} className="full-retour">
              Retour
            </p>
          </>
        )}
      </div>
    </>
  );
}

export default ManageUser;
