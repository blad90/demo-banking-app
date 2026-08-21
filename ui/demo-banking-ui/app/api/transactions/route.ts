import { NextRequest, NextResponse } from "next/server";
import { auth } from "../auth/[...nextauth]/route";

export async function POST(request: NextRequest){
    try{
    const body = await request.json();
    const session = await auth();

    if (!session || !session.accessToken) {
        return NextResponse.json({ error: "Unauthorized" }, { status: 401 });
    }

    const response = await fetch(`${process.env.TRANSACT_SAGA_ORCHESTRATOR_API_URL}`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${session.accessToken}`,
        },
        body: JSON.stringify(body),
    });
    
    const result = await response.json();

    if (!response.ok) {
      return new Response(result, { status: response.status });
    }
    

    return NextResponse.json(result, { status: response.status });
    
    } catch(error){
        return NextResponse.json({ sucess: false, error: (error as Error).message }, { status: 500 });
    }
}