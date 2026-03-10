const chatForm = document.getElementById("chatForm");
const chatSubmit = document.getElementById("chatSubmit");
const proposalButton = document.getElementById("proposalButton");
const proposalSkeleton = document.getElementById("proposalSkeleton");
const chatStatus = document.getElementById("chatStatus");
const proposalStatus = document.getElementById("proposalStatus");

async function handleApiResponse(response) {
    if (response.ok) {
        return response.json();
    }

    const errorBody = await response.json().catch(() => ({ message: "Unexpected error" }));
    throw new Error(errorBody.message || "Unexpected error");
}

if (chatForm) {
    chatForm.addEventListener("submit", async (event) => {
        event.preventDefault();
        const portfolioId = chatForm.dataset.portfolioId;
        const messageField = document.getElementById("chatMessage");
        const message = messageField.value.trim();

        if (!message) {
            chatStatus.textContent = "相談内容を入力してください。";
            chatStatus.classList.add("error");
            messageField.setAttribute("aria-invalid", "true");
            return;
        }

        chatStatus.textContent = "AIに問い合わせ中です...";
        chatStatus.classList.remove("error");
        messageField.setAttribute("aria-invalid", "false");
        if (chatSubmit) {
            chatSubmit.disabled = true;
            chatSubmit.setAttribute("aria-busy", "true");
        }

        try {
            await handleApiResponse(await fetch(`/api/portfolios/${portfolioId}/chat`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({ message })
            }));

            window.location.reload();
        } catch (error) {
            chatStatus.textContent = error.message;
            chatStatus.classList.add("error");
            messageField.setAttribute("aria-invalid", "true");
            if (chatSubmit) {
                chatSubmit.disabled = false;
                chatSubmit.removeAttribute("aria-busy");
            }
        }
    });
}

if (proposalButton) {
    proposalButton.addEventListener("click", async () => {
        const portfolioId = proposalButton.dataset.portfolioId;
        proposalStatus.textContent = "提案書ドラフトを生成中です...";
        proposalStatus.classList.remove("error");
        proposalButton.disabled = true;
        proposalButton.setAttribute("aria-busy", "true");
        if (proposalSkeleton) {
            proposalSkeleton.hidden = false;
        }

        try {
            await handleApiResponse(await fetch(`/api/portfolios/${portfolioId}/proposal`, {
                method: "POST"
            }));

            window.location.reload();
        } catch (error) {
            proposalStatus.textContent = error.message;
            proposalStatus.classList.add("error");
            proposalButton.disabled = false;
            proposalButton.removeAttribute("aria-busy");
            if (proposalSkeleton) {
                proposalSkeleton.hidden = true;
            }
        }
    });
}
