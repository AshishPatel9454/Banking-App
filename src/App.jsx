import React, { useState } from "react";
import axios from "axios";

const API = "http://localhost:8080/api/accounts";

export default function App() {
  const [accountId, setAccountId] = useState("");
  const [accounts, setAccounts] = useState([]);
  const [account, setAccount] = useState(null);
  const [amount, setAmount] = useState("");
  const [transactions, setTransactions] = useState([]);
  const [newAccount, setNewAccount] = useState({ accountHolderName: "", balance: 0 });

  // Create account
  const createAccount = async () => {
    const res = await axios.post(API, newAccount);
    alert("Account created!");
    setNewAccount({ accountHolderName: "", balance: 0 });
  };

  // Get single account
  const getAccount = async () => {
    const res = await axios.get(`${API}/${accountId}`);
    setAccount(res.data);
  };

  // Get all accounts
  const getAllAccounts = async () => {
    const res = await axios.get(API);
    setAccounts(res.data);
  };

  // Deposit
  const deposit = async () => {
    await axios.put(`${API}/${accountId}/deposit`, { amount: parseFloat(amount) });
    alert("Deposit successful");
  };

  // Withdraw
  const withdraw = async () => {
    await axios.put(`${API}/${accountId}/withdraw`, { amount: parseFloat(amount) });
    alert("Withdraw successful");
  };

  // Delete
  const deleteAccount = async () => {
    await axios.delete(`${API}/${accountId}`);
    alert("Account deleted");
  };

  // Transactions
  const getTransactions = async () => {
    const res = await axios.get(`${API}/${accountId}/transactions`);
    setTransactions(res.data);
  };

  return (
    <div className="p-6 max-w-4xl mx-auto space-y-6">
      <h1 className="text-2xl font-bold">Banking App UI</h1>

      <div className="bg-white p-4 rounded shadow">
        <h2 className="font-semibold">Create Account</h2>
        <input
          className="border p-2 m-1"
          placeholder="Name"
          value={newAccount.accountHolderName}
          onChange={(e) => setNewAccount({ ...newAccount, accountHolderName: e.target.value })}
        />
        <input
          className="border p-2 m-1"
          type="number"
          placeholder="Initial Balance"
          value={newAccount.balance}
          onChange={(e) => setNewAccount({ ...newAccount, balance: parseFloat(e.target.value) })}
        />
        <button className="bg-green-500 text-white px-4 py-2 rounded" onClick={createAccount}>
          Create
        </button>
      </div>

      <div className="bg-white p-4 rounded shadow space-y-2">
        <h2 className="font-semibold">Account Actions</h2>
        <input
          className="border p-2"
          placeholder="Account ID"
          value={accountId}
          onChange={(e) => setAccountId(e.target.value)}
        />
        <div className="space-x-2">
          <button className="bg-blue-500 text-white px-3 py-1 rounded" onClick={getAccount}>
            Get Account
          </button>
          <button className="bg-purple-500 text-white px-3 py-1 rounded" onClick={getAllAccounts}>
            All Accounts
          </button>
          <button className="bg-yellow-500 text-white px-3 py-1 rounded" onClick={getTransactions}>
            Transactions
          </button>
          <button className="bg-red-500 text-white px-3 py-1 rounded" onClick={deleteAccount}>
            Delete
          </button>
        </div>
      </div>

      <div className="bg-white p-4 rounded shadow space-y-2">
        <h2 className="font-semibold">Deposit / Withdraw</h2>
        <input
          className="border p-2"
          type="number"
          placeholder="Amount"
          value={amount}
          onChange={(e) => setAmount(e.target.value)}
        />
        <div className="space-x-2">
          <button className="bg-green-600 text-white px-3 py-1 rounded" onClick={deposit}>
            Deposit
          </button>
          <button className="bg-orange-600 text-white px-3 py-1 rounded" onClick={withdraw}>
            Withdraw
          </button>
        </div>
      </div>

      {account && (
        <div className="bg-white p-4 rounded shadow">
          <h2 className="font-semibold">Account Details</h2>
          <pre>{JSON.stringify(account, null, 2)}</pre>
        </div>
      )}

      {accounts.length > 0 && (
        <div className="bg-white p-4 rounded shadow">
          <h2 className="font-semibold">All Accounts</h2>
          <ul className="list-disc pl-5">
            {accounts.map((acc) => (
              <li key={acc.id}>
                ID: {acc.id} | Holder: {acc.accountHolderName} | Balance: ₹{acc.balance}
              </li>
            ))}
          </ul>
        </div>
      )}

      {transactions.length > 0 && (
        <div className="bg-white p-4 rounded shadow">
          <h2 className="font-semibold">Transactions</h2>
          <ul className="list-disc pl-5">
            {transactions.map((txn) => (
              <li key={txn.id}>
                {txn.transactionType} ₹{txn.amount} on {new Date(txn.timestamp).toLocaleString()}
              </li>
            ))}
          </ul>
        </div>
      )}
    </div>
  );
}
